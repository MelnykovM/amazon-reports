package com.melnykovmihail.security_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Registration Request")
public class SignUpRequest {

    @Schema(description = "Username", example = "Mihail")
    @Size(min = 5, max = 50, message = "Username must contain from 5 to 50 characters")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Schema(description = "The email address", example = "mihail@gmail.com")
    @Size(min = 5, max = 255, message = "The email address must contain between 5 and 255 characters")
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Email address must be in the format user@example.com")
    private String email;

    @Schema(description = "Password", example = "password123")
    @Size(min = 3, max = 255, message = "Password length must be from 3 to 255 characters")
    private String password;
}