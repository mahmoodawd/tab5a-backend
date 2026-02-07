package dev.awd.tab5abackend.controller;

import dev.awd.tab5abackend.dto.response.UsersListResponseDto;
import dev.awd.tab5abackend.model.User;
import dev.awd.tab5abackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAvatar(
            @RequestParam("avatar") MultipartFile avatar,
            @AuthenticationPrincipal User user
    ) {

        String email = user.getEmail();

        String avatarUrl = userService.uploadAvatar(email, avatar);

        return ResponseEntity.ok(avatarUrl);
    }


    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersListResponseDto> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("{userId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> grantAdminPermissions(@PathVariable UUID userId) {
        userService.grantAdminPermissions(userId);
        return ResponseEntity.ok("Admin access granted");
    }

}