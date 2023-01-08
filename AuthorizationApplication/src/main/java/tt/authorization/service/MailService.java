package tt.authorization.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
public class MailService {
    private final String messageHtml;

    public MailService() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:templates/mail.html");
        messageHtml =  asString(resource);
    }

    public void sendEmail(String to) {

        String from = "testmailforedin@gmail.com";
        String password = "zzbvbnahqewcoikx";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setFrom(new InternetAddress("APaevskiy"));
            message.setSubject("Подтверждение авторизации ЭДиН");
            message.setContent(messageHtml, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
           e.printStackTrace();
        }
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
