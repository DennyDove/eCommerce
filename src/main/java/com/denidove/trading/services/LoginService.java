package com.denidove.trading.services;

import com.denidove.trading.dto.LoginDto;
import com.denidove.trading.entities.User;
import com.denidove.trading.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class LoginService {

    private final UserRepository userRepository;
    private final UserSessionService userSessionService;

    public LoginService(UserRepository userRepository, UserSessionService userSessionService) {
        this.userRepository = userRepository;
        this.userSessionService = userSessionService;

    }

    public void loginCheck (LoginDto loginDto) {
        User user = userRepository.findUserByLogin(loginDto.getLogin()).get();
        if(loginDto.getPassword().equals(user.getPassword())) {
            userSessionService.setLoginStatus(true);
            userSessionService.setUserId(user.getId());
            userSessionService.setUserName(user.getName());
            userSessionService.setUserInit(String.valueOf((user.getName().charAt(0)))); // получаем и сохраняем первую букву имени
        }
    }
}
