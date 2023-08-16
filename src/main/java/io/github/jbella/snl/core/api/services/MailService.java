package io.github.jbella.snl.core.api.services;

import org.springframework.mail.javamail.MimeMessageHelper;

public interface MailService {
    void sendEmail(String from, String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendEmail(MimeMessageHelper message);

    MimeMessageHelper getMimeMessageHelper(boolean isMultipart);
}
