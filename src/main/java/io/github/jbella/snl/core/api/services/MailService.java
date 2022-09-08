package io.github.jbella.snl.core.api.services;

import java.util.Map;

public interface MailService {
    void sendEmail(String from, String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendEmailFromTemplate(String email, String templateName, Map<String, Object> variables);
}