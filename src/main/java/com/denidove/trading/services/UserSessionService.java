package com.denidove.trading.services;

import com.denidove.trading.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Getter
@Setter
@Service
@SessionScope
public class UserSessionService {

    private final UserRepository userRepository;

    private Boolean loginStatus = false;
    private Long userId;
    private String userName;

    public UserSessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
