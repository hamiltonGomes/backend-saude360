package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.entities.users.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String from;

    @Value("${mail.from.name}")
    private String fromName;

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    public static final String PNG_MINE = "image/png";

    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmailRegister(User user) throws MessagingException, UnsupportedEncodingException {

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(user.getEmail());

        email.setFrom(new InternetAddress(from, fromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());

        ctx.setVariable("name", user.getFullName());

        final String htmlContent;
        if(user instanceof Professional) {
            email.setSubject("Bem-vindo ao Saúde360 - Profissional");
            htmlContent = templateEngine.process("professionalRegister", ctx);
        } else {
            email.setSubject("Bem-vindo ao Saúde360 - Paciente");
            ctx.setVariable("password", user.getPassword());

            htmlContent = templateEngine.process("templates/patientRegister", ctx);
        }

        email.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

}
