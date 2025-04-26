package com.denidove.trading.services;

import com.denidove.trading.entities.User;

import java.util.Optional;

public interface UserService {
    public void save(User user);
    public Optional<User> findById(Long id);

}
