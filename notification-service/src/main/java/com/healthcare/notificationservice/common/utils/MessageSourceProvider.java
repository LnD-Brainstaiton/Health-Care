package com.healthcare.notificationservice.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceProvider {

    private static MessageSource messageSource;

    public MessageSourceProvider(MessageSource messageSource) {
        MessageSourceProvider.messageSource = messageSource;
    }

    public static String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }
}
