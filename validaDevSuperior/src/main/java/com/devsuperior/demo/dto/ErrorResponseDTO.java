package com.devsuperior.demo.dto;

import java.time.Instant;

public record ErrorResponseDTO(int code, String exception, String message, String path, Instant timestamp) {
}
