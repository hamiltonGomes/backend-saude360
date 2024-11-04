package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.data.UserDetailsSecurity;
import com.saude360.backendsaude360.dtos.LoginTokenResponseDto;
import com.saude360.backendsaude360.dtos.UserAuthenticationDto;
import com.saude360.backendsaude360.security.JWTToken;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/authentication")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JWTToken jwtToken;

    public AuthenticationController(AuthenticationManager authenticationManager, JWTToken jwtToken) {
        this.authenticationManager = authenticationManager;
        this.jwtToken = jwtToken;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginTokenResponseDto> login(@RequestBody @Valid UserAuthenticationDto userAuthenticationDto) {
        var usuarioEmailSenha = new UsernamePasswordAuthenticationToken(userAuthenticationDto.cpf(), userAuthenticationDto.password());
        var auth = authenticationManager.authenticate(usuarioEmailSenha);
        var token = jwtToken.generateToken((UserDetailsSecurity) auth.getPrincipal());
        var roles = ((UserDetailsSecurity) auth.getPrincipal()).getAuthorities();
        return ResponseEntity.ok(new LoginTokenResponseDto(userAuthenticationDto.cpf(), token, (List<GrantedAuthority>) roles));
    }
}
