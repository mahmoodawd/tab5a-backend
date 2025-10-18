package dev.awd.tab5abackend.dto;

import java.time.Instant;

public record ErrorResponse(String message, Instant timestamp) {
}
