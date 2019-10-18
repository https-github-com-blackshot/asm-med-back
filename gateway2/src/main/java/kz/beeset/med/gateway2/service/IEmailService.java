package kz.beeset.med.gateway2.service;


import kz.beeset.med.gateway2.util.error.InternalException;

public interface IEmailService {
    void sendMail(String toEmail, String subject, String message) throws InternalException;
    void sendMailToEmails(String[] toEmails, String subject, String message) throws InternalException;
    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws InternalException;
}
