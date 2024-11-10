package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.data.UserDetailsSecurity;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.exceptions.TokenInvalidException;
import com.saude360.backendsaude360.repositories.users.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByCpf(login);

        if (user.isEmpty()) {
            throw new TokenInvalidException("Usuário: " + login + " não foi encontrado.");
        }
        return new UserDetailsSecurity(user);
    }
}
