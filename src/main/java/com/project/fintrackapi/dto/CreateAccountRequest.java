package com.project.fintrackapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank(message = "Account must have a name")
        String name,
        @Email(message = "Account email must be a valid email address")
        @NotBlank(message = "Account must have a valid email address")
        String email
) {
}
