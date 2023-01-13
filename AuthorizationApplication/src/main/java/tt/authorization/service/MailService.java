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
import tt.authorization.entity.security.User;

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

    public MailService(JavaMailSender mailSender) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:templates/mail.html");
        messageHtml = asString(resource);
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        helper.setTo(to);
        helper.setSubject("Регистрация в ЭДиН");
        int index = messageHtml.indexOf("_message_");
        StringBuilder builder = new StringBuilder(messageHtml.replaceAll("_message_", ""));
        builder.insert(index, message);

        helper.setText( builder.toString(), true);
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
        String message = String.format("<p>Вы получили это письмо, потому что кто-то (возможно, и Вы :)" +
                "указал этот e-mail для регистрации на платформе ЭДиН." +
                "Чтобы подтвердить регистрацию, перейдите, пожалуйста, по ссылке:</p>" +
                "<a href=\"%s\">%s</a>" +
                "<p>Если это были не Вы, просто проигнорируйте наше письмо.</p>",
                "http://"+ InetAddress.getLocalHost().getHostAddress() + ":" + serverPort + "/activate/"+activationCode,
                "/activate/"+activationCode);

        sendEmail(username, message);
    }

    public void sendRecoveryEmail(String username, String activationCode) throws UnknownHostException, MessagingException {
        String message = String.format("<p>Вы получили это письмо, потому что кто-то пытается" +
                "    восстановить пароль от аккаунта на платформе ЭДиН." +
                "    Чтобы восстановить пароль, перейдите, пожалуйста, по ссылке:</p>" +
                "<a href=\"%s\">%s</a>" +
                "<p>Если это были не Вы, просто проигнорируйте наше письмо.</p>",
                "http://"+ InetAddress.getLocalHost().getHostAddress() + ":" + serverPort + "/activate/"+activationCode,
                "/activate/"+activationCode);

        sendEmail(username, message);
    }

    public void sendRegistrationByAdmin(User user) throws MessagingException {
        String message = String.format("<p>Вас зарегистрировали на платформе " +
                "  <a href=\"https://app.edn.by/web/\">ЭДиН</a>." +
                "  Ваша учётная запись:</p>" +
                "<div style=\"background-color: rgba(149,161,183,0.62)\">" +
                "  <p>Логин: %s</p>" +
                "  <p>Пароль: %s</p>" +
                "</div>", user.getUsername(), user.getPassword());
        sendEmail(user.getUsername(), message);
    }
}
