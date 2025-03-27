package com.denidove.trading.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class LoginDto {

    private String login;
    private String password;

    public LoginDto(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
