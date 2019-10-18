package kz.beeset.med.admin.service.impl;

import kz.beeset.med.admin.service.IEmailService;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.error.InternalExceptionHelper;
import kz.beeset.med.admin.utils.error.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService implements IEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(String toEmail, String subject, String message) throws InternalException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

            mimeMessage.setContent(message, "text/html; charset=utf-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при отправке сообщения на " + toEmail, e,
                    new Pair("toEmail", toEmail), new Pair("subject", subject));
        }

    }

    @Override
    public void sendMailToEmails(String[] toEmails, String subject, String message) throws InternalException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

            mimeMessage.setContent(message, "text/html; charset=utf-8");
            helper.setBcc(toEmails);
            helper.setSubject(subject);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            LOGGER.error("Ошибка при отправке сообщения " + e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при отправке сообщения на " + toEmails, e,
                    new Pair("toEmails", toEmails), new Pair("subject", subject));
        }

    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws InternalException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("ApplicationReceipt", file);

            javaMailSender.send(message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при отправке сообщения на " + to, e,
                    new Pair("toEmail", to), new Pair("subject", subject));
        }

    }
}
