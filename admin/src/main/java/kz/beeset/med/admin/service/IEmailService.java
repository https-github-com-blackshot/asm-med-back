package kz.beeset.med.admin.service;

import kz.beeset.med.admin.utils.error.InternalException;

public interface IEmailService {

    void sendMail(String toEmail, String subject, String message) throws InternalException;
    void sendMailToEmails(String[] toEmails, String subject, String message) throws InternalException;
    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws InternalException;
}
