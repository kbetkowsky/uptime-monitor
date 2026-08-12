package com.kbetkowski.uptimemonitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateSiteRequest(
        @NotBlank
        String name,
        @NotBlank
        @Pattern(regexp = "^https?://.+")
        String url
) {
}
