package com.chriskocabas.redditclone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Email
    @NotEmpty(message = "Email is required")
    private String email;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @Size.List({
            @Size(min = 2, message = "{validation.firstname.size.too_short}"),
            @Size(max = 50, message = "{validation.firstname.size.too_long}")
    })
    @NotBlank(message = "Firstname is required")
    private String firstname;
    @Size.List({
            @Size(min = 2, message = "{validation.lastname.size.too_short}"),
            @Size(max = 50, message = "{validation.lastname.size.too_long}")
    })
    @NotBlank(message = "Lastname is required")
    private String lastname;

}