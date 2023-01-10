package tt.authorization.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
public class MailService  {
    private final String messageHtml;
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:templates/mail.html");
        messageHtml =  asString(resource);
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String key) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("");
        message.setTo(to);
        message.setSubject("Подтверждение авторизации ЭДиН");
        message.setText(messageHtml.replaceAll("key", key));

        mailSender.send(message);
    }
    private String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UncheckedIOException(e);
        }
    }
}
