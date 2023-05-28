package org.example.home.dto;

import lombok.Data;

@Data
public class DtoRegister {

    private String email;
    private String username;
    private String password;
    private String userRole;
}