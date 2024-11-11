package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.entities.PasswordReset;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
            htmlContent = templateEngine.process("email-welcome-professional", ctx);
        } else {
            email.setSubject("Bem-vindo ao Saúde360 - Paciente");

            htmlContent = templateEngine.process("email-welcome-patient", ctx);
        }

        email.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendEmailForgetPassword(PasswordReset passwordReset) throws MessagingException, UnsupportedEncodingException {

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(passwordReset.getUser().getEmail());

        email.setFrom(new InternetAddress(from, fromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());

        ctx.setVariable("name", passwordReset.getUser().getFullName());
        ctx.setVariable("code", passwordReset.getCode());

        email.setSubject("Recuperação de senha - Saúde360");

        final String htmlContent = templateEngine.process("email-verification-code", ctx);

        email.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendEmailAppointmentConfirmation(Consultation consultation) throws MessagingException, UnsupportedEncodingException {

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(consultation.getPatient().getEmail());

        email.setFrom(new InternetAddress(from, fromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDate = consultation.getDate().format(formatter);

        LocalDateTime startTime = LocalDateTime.ofInstant(consultation.getStartServiceDateAndTime(), ZoneId.systemDefault());
        LocalDateTime endTime = LocalDateTime.ofInstant(consultation.getEndServiceDateAndTime(), ZoneId.systemDefault());
        String formatedTimeStart = startTime.format(formatterTime);
        String formatedTimeEnd = endTime.format(formatterTime);

        String address = (consultation.getProfessional().getClinics() != null &&  !consultation.getProfessional().getClinics().isEmpty()) ? consultation.getProfessional().getClinics().getFirst().getAddress().toString() : "";

        ctx.setVariable("name", consultation.getPatient().getFullName());
        ctx.setVariable("data", String.format("%s", formattedDate));
        ctx.setVariable("horaInicio", formatedTimeStart);
        ctx.setVariable("horaFim", formatedTimeEnd);
        ctx.setVariable("nomeProfissional", consultation.getProfessional().getFullName());
        ctx.setVariable("endereco", address);

        email.setSubject("Agendamento de consulta - Saúde360");

        final String htmlContent = templateEngine.process("email-appointment-confirmation", ctx);

        email.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

}
