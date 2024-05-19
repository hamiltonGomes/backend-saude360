package com.saude360.backendsaude360.controllers;

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

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        var user = userService.findById(id);
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
}
