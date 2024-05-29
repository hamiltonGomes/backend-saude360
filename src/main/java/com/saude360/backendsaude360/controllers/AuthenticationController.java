package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.data.UserDetailsSecurity;
import com.saude360.backendsaude360.dtos.LoginTokenResponseDto;
import com.saude360.backendsaude360.dtos.UserAuthenticationDto;
import com.saude360.backendsaude360.security.JWTToken;
import com.saude360.backendsaude360.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/authentication")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTToken jwtToken;

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody @Valid UserAuthenticationDto userAuthenticationDto) {
        var usuarioEmailSenha = new UsernamePasswordAuthenticationToken(userAuthenticationDto.cpf(), userAuthenticationDto.password());
        var auth = authenticationManager.authenticate(usuarioEmailSenha);
        var token = jwtToken.generateToken((UserDetailsSecurity) auth.getPrincipal());
        return ResponseEntity.ok(new LoginTokenResponseDto(userAuthenticationDto.cpf(), token));
    }
}
