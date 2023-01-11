package tt.authorization.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
public class MailService {
    private final String messageHtml;
    private final JavaMailSender mailSender;
    @Value("${server.port}")
    private String serverPort;
    @Value("${spring.mail.subject.message}")
    private String subjectMessage;
    @Value("${spring.mail.recovery.password.message}")
    private String recoveryMessage;
    @Value("${spring.mail.registration.message}")
    private String registrationMessage;

    public MailService(JavaMailSender mailSender) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:templates/mail.html");
        messageHtml = asString(resource);
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String key, String message) throws MessagingException, UnknownHostException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        helper.setTo(to);
        helper.setSubject(subjectMessage);
        helper.setText(
                messageHtml.replaceAll("keyUrl", "http://"+ InetAddress.getLocalHost().getHostAddress() + ":" + serverPort + key)
                .replaceAll("key", key)
                        .replaceAll("message", message), true);
        this.mailSender.send(mimeMessage);
    }

    private String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UncheckedIOException(e);
        }
    }

    public void sendRegistrationEmail(String username, String activationCode) throws UnknownHostException, MessagingException {
        sendEmail(username, "/activate/"+activationCode, registrationMessage);
    }

    public void sendRecoveryEmail(String username, String activationCode) throws UnknownHostException, MessagingException {
        sendEmail(username, "/recovery/"+activationCode, recoveryMessage);
    }
}
