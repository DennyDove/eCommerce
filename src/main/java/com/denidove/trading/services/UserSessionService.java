package com.denidove.trading.services;

import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
@SessionScope
public class UserSessionService {

    private final UserRepository userRepository;

    private Boolean authStatus = false; // устанавливаем по умолчанию статус аутентификации false
    private Long userId;
    private String userName;
    private String userInit;  // первая буква имени

    private List<CartItemDto> cartItemDtoList = new ArrayList<>();

    public UserSessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
