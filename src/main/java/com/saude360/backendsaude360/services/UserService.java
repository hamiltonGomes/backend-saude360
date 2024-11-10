package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ResetPasswordDto;
import com.saude360.backendsaude360.entities.PasswordReset;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.exceptions.ResetPasswordInvalidException;
import com.saude360.backendsaude360.repositories.PasswordResetRepository;
import com.saude360.backendsaude360.repositories.users.UserRepository;
import com.saude360.backendsaude360.utils.BCryptPassword;
import com.saude360.backendsaude360.utils.GeneratorRandom;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailService emailService;

    private static final String USER_NOT_FOUND_MSG = "Usuário com ID: %d não foi encontrado.";
    private static final String USER_CPF_NOT_FOUND_MSG = "Usuário com cpf: %s não foi encontrado.";
    private static final String USER_CPF_RESET_NOT_FOUND_MSG = "Usuário com CPF: %s não foi encontrado.";
    private static final String PASSWORD_RESET_INVALID_CODE_MSG = "Código de recuperação de senha inválido.";
    private static final String PASSWORD_RESET_EXPIRED_CODE_MSG = "Código de recuperação de senha expirado.";

    @Autowired
    public UserService(UserRepository userRepository, PasswordResetRepository passwordResetRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.emailService = emailService;
    }

    public User create(User user) {
        var obj = userRepository.save(user);

        try {
            emailService.sendEmailRegister(obj);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return user;
        }

        return obj;
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format(USER_NOT_FOUND_MSG, id));
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void forgetPassword(String cpf) {
        var user = userRepository.findByCpf(cpf);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException(String.format(USER_CPF_NOT_FOUND_MSG, cpf));
        }
        String code = GeneratorRandom.generateCode();

        boolean codeExist = false;

        do {
            codeExist = passwordResetRepository.findByCode(code) != null;
            if (codeExist) {
                code = GeneratorRandom.generateCode();
            }
        } while (codeExist);

        var passwordReset = new PasswordReset(code, user.get());

        try {
            passwordResetRepository.save(passwordReset);
        } catch (DataIntegrityViolationException e) {
            var oldCode = passwordResetRepository.findByUser(user.get());
            passwordResetRepository.delete(oldCode);
            passwordResetRepository.save(passwordReset);
        }

        try {
            emailService.sendEmailForgetPassword(passwordReset);
        } catch (MessagingException | UnsupportedEncodingException e) {
//
        }

    }

    public void resetPassowrd(ResetPasswordDto resetPasswordDto) {
        var passwordReset = passwordResetRepository.findByCode(resetPasswordDto.code());
        if (passwordReset == null) {
            throw new ResetPasswordInvalidException(PASSWORD_RESET_INVALID_CODE_MSG);
        }
        Date now = new Date();
        if (passwordReset.isExpired(now)) {
            throw new ResetPasswordInvalidException(PASSWORD_RESET_EXPIRED_CODE_MSG);
        }
        var user = passwordReset.getUser();
        user.setPassword(BCryptPassword.encryptPassword(resetPasswordDto.password()));

        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);
    }

    public User findByCpf(String cpf) {
        return userRepository.findByCpf(cpf).orElseThrow(() -> new ObjectNotFoundException(String.format(USER_CPF_RESET_NOT_FOUND_MSG, cpf)));
    }
}
