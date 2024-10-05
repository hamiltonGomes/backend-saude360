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

    @Autowired
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordResetRepository passwordResetRepository) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
    }

    public User create(User user) {
        var obj =  userRepository.save(user);

        try {
            emailService.sendEmailRegister(obj);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return user;
        }

        return obj;
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("Usuário com ID: " + id + " não foi encontrado."));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Usuário com ID: " + id + " não foi encontrado.");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void forgetPassword(String cpf) {
        var user = userRepository.findByCpf(cpf);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Usuário com cpf: " + cpf + " não foi encontrado.");
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
        passwordResetRepository.save(passwordReset);

        try {
            emailService.sendEmailForgetPassword(passwordReset);
        } catch (MessagingException | UnsupportedEncodingException e) {
//
        }

    }

    public void resetPassowrd(ResetPasswordDto resetPasswordDto) {
        var passwordReset = passwordResetRepository.findByCode(resetPasswordDto.code());
        if (passwordReset == null) {
            throw new ResetPasswordInvalidException("Código de recuperação de senha inválido.");
        }
        Date now = new Date();
        if(passwordReset.isExpired(now)) {
            throw new ResetPasswordInvalidException("Código de recuperação de senha expirado.");
        }
        var user = passwordReset.getUser();
        user.setPassword(BCryptPassword.encryptPassword(resetPasswordDto.password()));

        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);
    }
}
