package kz.beeset.med.gateway2.service;

import kz.beeset.med.admin.model.User;
import kz.beeset.med.gateway2.dto.UserDTO;
import kz.beeset.med.gateway2.model.EmailText;
import kz.beeset.med.gateway2.repository.EmailTextRepository;
import kz.beeset.med.gateway2.repository.UsersRepository;
import kz.beeset.med.gateway2.util.error.ErrorCode;
import kz.beeset.med.gateway2.util.error.InternalException;
import kz.beeset.med.gateway2.util.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    private UsersRepository usersRepository;
    private EmailTextRepository emailTextRepository;
    private IEmailService emailService;


    @Autowired
    public UsersService(UsersRepository usersRepository, EmailTextRepository emailTextRepository, IEmailService emailService) {
        this.usersRepository = usersRepository;
        this.emailTextRepository = emailTextRepository;
        this.emailService = emailService;
    }

    public User findUserByIdn(String idn) throws DataAccessException {

        User user = null;

        try {
            user = usersRepository.findByIdn(idn);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return user;
    }

    public User findBySignupToken(String token) throws DataAccessException {
        return usersRepository.findBySignupToken(token);
    }

    public User findUserById(String id) throws DataAccessException {

        User user = null;

        try {

            user = usersRepository.findUserById(id);

        } catch (EmptyResultDataAccessException e) {

            return null;
        }

        return user;
    }

    public User findUserByIdnOrMobilePhone(String idnOrMobilePhone) throws DataAccessException {

//        System.out.println("idnOrMobilePhone: " + idnOrMobilePhone);

        User user = null;

        try {

            user = usersRepository.findUserByIdnOrMobilePhoneNumber(idnOrMobilePhone);

//            System.out.println("user: " + user);

        } catch (EmptyResultDataAccessException e) {

            return null;
        }

        return user;
    }

    public User findUserByIdnOrMobilePhoneOrUsernameOrEmail(String idnOrMobilePhoneOrUsernameOrEmail) throws DataAccessException {
        User user = null;
        try {
            user = usersRepository.getByUsernameOrIdnOrMobilePhoneOrEmail(idnOrMobilePhoneOrUsernameOrEmail);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }

    public User create(User user) {
        return usersRepository.save(user);
    }

    public List<User> read() {
        return usersRepository.findAll();
    }

    public User update(User user) throws InternalException {
        try {
            return usersRepository.save(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при обновлении данных пользователя.", e);
        }
    }


    public void delete(User user) {
        usersRepository.delete(user);
    }

    public String sendEmailConfirmLink(UserDTO dto) throws InternalException {
        try {
            String signupToken = UUID.randomUUID().toString();
            EmailText emailText = emailTextRepository.findByCode("registration_confirm");

            String message = emailText.getBodyRu().replaceAll("%lastname", dto.getSurname())
                    .replaceAll("%firstname", dto.getName())
                    .replaceAll("%iin", dto.getIdn())
                    .replaceAll("%confirm_link", "https://cloudoc.kz/#/auth/confirmed?token=" + signupToken);
            LOGGER.debug("Email message: " + message);
            emailService.sendMail(dto.getEmail(), emailText.getHeaderRu(), message);

            return signupToken;
        } catch (InternalException ie) {
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:AuthService.sendEmailConfirmLink", e);
        }
    }

    public String sendMailResetPassword(UserDTO dto) throws InternalException {
        try {
            String signupToken = UUID.randomUUID().toString();
            EmailText emailText = emailTextRepository.findByCode("reset_password");
            LOGGER.info("emailText = " + emailText);

            String message = emailText.getBodyRu().replaceAll("%lastname", dto.getSurname())
                    .replaceAll("%firstname", dto.getName())
                    .replaceAll("%confirm_link", "https://cloudoc.kz/#/auth/resetPassword?token=" + signupToken);
            LOGGER.debug("Email message: " + message);
            emailService.sendMail(dto.getEmail(), emailText.getHeaderRu(), message);

            return signupToken;
        } catch (InternalException ie) {
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:AuthService.sendMailResetPassword", e);
        }
    }

}
