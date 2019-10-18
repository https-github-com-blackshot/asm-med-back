package kz.beeset.med.dmp.utils;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class AuthUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUtil.class);
    private static final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper("AuthUtil");

    public static String checkPasswordsAndHash(String password, String repassword) throws InternalException {
        try {
            if (!password.equals(repassword)) {
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.PASSWORDS_NOT_MATCH, "Пароли не совпадают.");
            }

            return sha256(password);
        } catch (InternalException ie) {
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка пароля.", e);
        }

    }

    public static Boolean checkPasswordHash(String password, String hash) throws InternalException {
        try {
            if (!hash.equalsIgnoreCase(sha256(password))) {
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.AUTH_ERROR, "Ошибка авторизации. 2");
            }

            return true;
        } catch (InternalException ie) {
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка авторизации. 3", e);
        }

    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
