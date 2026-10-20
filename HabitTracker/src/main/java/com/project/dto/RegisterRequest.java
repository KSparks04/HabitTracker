package com.project.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class
RegisterRequest {
    @NotBlank
    @Size(min=5,max=10,message="Username must be at least 5 characters long")
    private String username;
    @NotBlank(message = "Email cannot be blank")
    @Email
    private String email;
    @NotBlank
    @Size(min=8,message = "Password must be at least 8 characters long")
    private String password;

}
