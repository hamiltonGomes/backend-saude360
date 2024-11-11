package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ResetPasswordDto;
import com.saude360.backendsaude360.entities.PasswordReset;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.exceptions.ResetPasswordInvalidException;
import com.saude360.backendsaude360.repositories.PasswordResetRepository;
import com.saude360.backendsaude360.repositories.users.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetRepository passwordResetRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setCpf("12345678901");
        user.setPassword("oldPassword");

        new PasswordReset("validCode", user);
    }

    @Test
    void testCreateUser() throws MessagingException, UnsupportedEncodingException {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        verify(emailService).sendEmailRegister(createdUser);
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void testDelete_UserNotFound() {
        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(anyLong());

        assertThrows(ObjectNotFoundException.class, () -> userService.delete(1L));
    }

    @Test
    void testDelete_Success() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testForgetPassword_UserNotFound() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.forgetPassword("12345678901"));
    }

    @Test
    void testForgetPassword_Success() throws MessagingException, UnsupportedEncodingException {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(user));
        when(passwordResetRepository.findByCode(anyString())).thenReturn(null);

        userService.forgetPassword("12345678901");

        verify(passwordResetRepository).save(any(PasswordReset.class));
        verify(emailService).sendEmailForgetPassword(any(PasswordReset.class));
    }

    @Test
    void testResetPassword_InvalidCode() {
        ResetPasswordDto resetPasswordRequest = new ResetPasswordDto("invalidCode", "newPassword");

        when(passwordResetRepository.findByCode(anyString())).thenReturn(null);

        assertThrows(ResetPasswordInvalidException.class, () -> userService.resetPassowrd(resetPasswordRequest));
    }

    @Test
    void testFindByCpf_UserNotFound() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.findByCpf("12345678901"));
    }

    @Test
    void testFindByCpf_Success() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.findByCpf("12345678901");

        assertNotNull(foundUser);
        assertEquals(user.getCpf(), foundUser.getCpf());
    }
}
