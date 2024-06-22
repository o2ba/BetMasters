package service.general.springService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

/**
 * Utility class for handling language. Meant for Spring Boot applications.
 */
public final class SpringLanguageUtil {

    /** logger for the SpringLanguageUtil class */
    private static final Logger logger = LoggerFactory.getLogger(SpringLanguageUtil.class);

    /** the message source */
    private final MessageSource messageSource;

    public SpringLanguageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get a message from the message source, depending on the language provided.
     * If the language is not valid, it will default to English.
     * @param messageTag the tag of the message
     * @param language the language of the message
     * @return the message or an empty string if the message is not found
     */
    public String getMessage(String messageTag, String language) {
        Locale locale;
        Locale locale_en = Locale.forLanguageTag("en");

        try {
            locale = Locale.forLanguageTag(language);
        } catch (NullPointerException e) {
            locale = locale_en;
        }

        try {
            return messageSource.getMessage(messageTag, null, locale);
        } catch (NoSuchMessageException e) {
            logger.warn("Message {} not found for locale {}", messageTag, locale);
            try {
                return messageSource.getMessage(messageTag, null, locale_en);
            } catch (NoSuchMessageException e2) {
                logger.error("Message {} not found for locale {}", messageTag, locale_en);
                return "";
            }
        }

    }
}