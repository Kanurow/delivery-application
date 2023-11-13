package com.rowland.engineering.byteworks.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 2,max = 40, message = "Name should be between two characters and forty characters")
    private String name;


    @NotBlank(message ="Username must not be blank")
    @Size(min = 2,max = 40 , message = "Username should be between two characters and forty characters")
    private String username;


    @Email(message = "Enter a valid email")
    @NaturalId
    private String email;

    @NotBlank(message ="Mobile must not be blank")
    @Size(min = 4)
    private String password;

}
