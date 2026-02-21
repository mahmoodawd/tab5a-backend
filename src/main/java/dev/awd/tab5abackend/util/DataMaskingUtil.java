package dev.awd.tab5abackend.util;

import org.springframework.stereotype.Component;

@Component
public class DataMaskingUtil {

    public String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];

        String maskedLocal = localPart.length() > 2
                ? localPart.charAt(0) + "***" + localPart.charAt(localPart.length() - 1)
                : "***";

        String maskedDomain = domain.length() > 4
                ? domain.charAt(0) + "*****" + domain.substring(domain.length() - 3)
                : "***";

        return maskedLocal + "@" + maskedDomain;
    }

    public String maskPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return "***";
        }

        String cleaned = phone.replaceAll("[^0-9+]", "");

        if (cleaned.length() < 4) {
            return "***";
        }

        // +1234567890 -> +1******890
        // 01234567890 -> 012****7890
        if (cleaned.startsWith("+")) {
            int visibleStart = Math.min(2, cleaned.length() - 3);
            return cleaned.substring(0, visibleStart) +
                    "*".repeat(Math.max(0, cleaned.length() - visibleStart - 3)) +
                    cleaned.substring(cleaned.length() - 3);
        } else {
            int visibleStart = Math.min(3, cleaned.length() - 4);
            return cleaned.substring(0, visibleStart) +
                    "*".repeat(Math.max(0, cleaned.length() - visibleStart - 4)) +
                    cleaned.substring(cleaned.length() - 4);
        }
    }

    public String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return "***";
        }

        String[] parts = name.split("\\s+");
        StringBuilder masked = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                masked.append(" ");
            }

            String part = parts[i];
            if (part.length() > 1) {
                masked.append(part.charAt(0)).append("***");
            } else {
                masked.append("*");
            }
        }

        return masked.toString();
    }
}