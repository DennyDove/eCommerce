package com.denidove.trading.services;

import com.denidove.trading.entities.Role;
import com.denidove.trading.entities.User;
import com.denidove.trading.repositories.RoleRepository;
import com.denidove.trading.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(User user) {

        Role userRole = roleRepository.getReferenceById(1);
        user.setRole(userRole);

        userRepository.save(user);
    }
}
