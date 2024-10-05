package com.saude360.backendsaude360.exceptions;

public class ResetPasswordInvalidException extends RuntimeException {
    public ResetPasswordInvalidException(String message) {
        super(message);
    }
}
