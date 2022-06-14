package com.ldnhat.springbootblog.dto;

import lombok.Data;

@Data
public class SignupDto {

    private String name;
    private String username;
    private String email;
    private String password;
}
