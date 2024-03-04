package com.melnykovmihail.security_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Authentication request")
public class SignInRequest {

    @Schema(description = "Username", example = "Mihail")
    @Size(min = 5, max = 50, message = "Username must contain from 5 to 50 characters")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Password", example = "password123")
    @Size(min = 3, max = 255, message = "Password length must be from 3 to 255 characters")
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
