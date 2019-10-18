package kz.beeset.med.admin.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.beeset.med.admin.model.common.UserSignupModel;
import kz.beeset.med.admin.service.IEmailService;
import kz.beeset.med.admin.utils.CommonService;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.validators.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/menu")
public class AuthController  extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IEmailService emailService;

    @PostMapping("/signup")
    @ApiOperation("Авторизация в систему")
    public ResponseEntity<?> signup(@ApiParam("Регистрационная форма пользователя") @Valid @RequestBody UserSignupModel model) {
        try {
            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.validate(model.getEmail())) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Неверный e-mail формат"));
            }
            //TODO: mail conf info
            return builder(success(model));
        } catch (Exception e) {
            LOGGER.error("ERROR - User signing up failed - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при регистрации."));
        }
    }
    @PutMapping("/resetpassword/{username}")
    @ApiOperation("Сброс пароля")
    public ResponseEntity<?> resetPassword(@ApiParam("Имя пользователя") @PathVariable(value = "username") String username) {
//        System.out.println("=====================================");
        LOGGER.info("APP-LOG - Reset password...");

        //TODO:
        return builder(success("TODO"));


    }
    @PostMapping("/test/mail/{message}/{to}")
    @ApiOperation("Тестируем функционал отправки сообщений")
    public ResponseEntity<?> testSendMessage(
            @ApiParam("Текст сообщений") @PathVariable(name = "message") String message,
            @ApiParam("Кому отправить") @PathVariable(name = "to") String to){
        try {
            emailService.sendMail(to,"hz mz",message);
            return builder(success("Message !!!"));
        } catch (Exception e) {
            LOGGER.error("ERROR - User signing up failed - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при регистрации."));
        }
        }

    }

