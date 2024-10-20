package com.saude360.backendsaude360.controllers.exceptions;

import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.exceptions.ResetPasswordInvalidException;
import com.saude360.backendsaude360.exceptions.TokenInvalidException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    public static final String TIME_ZONE = "America/Sao_Paulo";

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        String error = "Objeto não encontrado.";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(ZonedDateTime.now(ZoneId.of(TIME_ZONE)), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException e, HttpServletRequest request) {
        String error = "Erro no banco de dados.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(ZonedDateTime.now(ZoneId.of(TIME_ZONE)), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler({EntityNotFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<StandardError> handle404Error(EntityNotFoundException entity, NoHandlerFoundException noHandler, HttpServletRequest request) {
        String error = "Route não encontrada.";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(ZonedDateTime.now(ZoneId.of(TIME_ZONE)), status.value(), error, (entity != null) ? entity.getMessage() : noHandler.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<StandardError> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String error = "Erro de validação.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getBindingResult().getFieldErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).orElse(e.getMessage());
        StandardError standardError = new StandardError(ZonedDateTime.now(ZoneId.of(TIME_ZONE)), status.value(), error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler({ResetPasswordInvalidException.class})
    public ResponseEntity<StandardError> handleTimeExpired(ResetPasswordInvalidException e, HttpServletRequest request) {
        String error = "Código inválido.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(ZonedDateTime.now(ZoneId.of(TIME_ZONE)), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler({TokenInvalidException.class})
    public ResponseEntity<StandardError> handleTokenInvalid(TokenInvalidException e, HttpServletRequest request) {
        String error = "Token inválido.";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError standardError = new StandardError(ZonedDateTime.now(ZoneId.of(TIME_ZONE)), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
}
