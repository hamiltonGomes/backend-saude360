package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.ResetPasswordDto;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{cpf}")
    public ResponseEntity<User> findUserByCpf(@PathVariable String cpf) {
        var user = userService.findByCpf(cpf);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<User>> findAllUsers() {
        var users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/forget-password/{cpf}")
    public ResponseEntity<User> forgetPassword(@PathVariable String cpf) {
        userService.forgetPassword(cpf);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<User> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {

        userService.resetPassowrd(resetPasswordDto);
        return ResponseEntity.noContent().build();
    }
}
