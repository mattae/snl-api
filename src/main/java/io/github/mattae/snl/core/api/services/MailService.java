package io.github.mattae.snl.core.api.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public interface MailService {

    void sendEmail(String from, String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendEmail(MimeMessageHelper message);


    default MimeMessageHelper getMimeMessageHelper(boolean isMultipart) {
        return null;
    }


    default JavaMailSender mailSender() {
        return null;
    }
}
