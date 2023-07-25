package com.chriskocabas.redditclone.service;

import org.thymeleaf.context.Context;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@AllArgsConstructor
public class MailContentBuilder {
    private final TemplateEngine templateEngine;

    String build(String message, String recipient) {
        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("recipient", recipient);
        return templateEngine.process("registrationConfirmationMailTemplate", context);

    }
}
