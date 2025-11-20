package com.miniprojet.miniprojet.config;

import com.miniprojet.miniprojet.entity.ReservationEmail;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
@EnableIntegration
public class EmailIntegrationConfig {

    private final JavaMailSender mailSender;

    public EmailIntegrationConfig(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Bean
    public MessageChannel reservationEmailChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "reservationEmailChannel")//ecouteru qui ecoute le channel c'est une annotation de spring integration
    public void sendEmail(ReservationEmail email) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();//cree email vide
        MimeMessageHelper helper = new MimeMessageHelper(message, true);//pour remplir email
        helper.setTo(email.getDestinataire());
        helper.setSubject(email.getSujet());
        helper.setText(email.getCorps(), false);

        mailSender.send(message);

        System.out.println("E-mail envoyé à : " + email.getDestinataire());
    }
}
