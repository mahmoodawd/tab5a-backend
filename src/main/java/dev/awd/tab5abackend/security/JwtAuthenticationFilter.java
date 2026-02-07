package dev.awd.tab5abackend.security;

import dev.awd.tab5abackend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Lazy
    private final UserService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("=== JWT Filter Processing ===");
        log.info("Request URI: {}", request.getRequestURI());
        log.info("Request Method: {}", request.getMethod());

        final String authHeader = request.getHeader("Authorization");
        log.info("Authorization Header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No valid Authorization header - skipping JWT processing");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            log.info("Extracted JWT (first 20 chars): {}", jwt.substring(0, Math.min(20, jwt.length())));

            final String userEmail = jwtService.extractUsername(jwt);
            log.info("Extracted email from JWT: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Email found and no existing authentication - loading user details");

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                log.info("User details loaded: {}", userDetails.getUsername());
                log.info("User authorities: {}", userDetails.getAuthorities());

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    log.info("Token is valid - creating authentication");

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.info("✅ Authentication set successfully in SecurityContext");
                } else {
                    log.error("❌ Token validation failed");
                }
            } else {
                if (userEmail == null) {
                    log.error("❌ Could not extract email from token");
                }
                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    log.info("Authentication already exists in SecurityContext");
                }
            }
        } catch (Exception e) {
            log.error("❌ Error processing JWT token: ", e);
        }

        log.info("=== End JWT Filter Processing ===\n");
        filterChain.doFilter(request, response);
    }
}