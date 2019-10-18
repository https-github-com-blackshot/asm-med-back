package kz.beeset.med.gateway2.util.error;

public class ErrorCode {

    // HashMap params;
    public enum ErrorCodes {
        SYSTEM_ERROR,
        AUTH_ERROR,
        SESSION_TIMEOUT,
        PASSWORDS_NOT_MATCH,
        USER_NOT_FOUND,
        EMPTY_CREDENTIALS,
        NOT_AUTHENTICATED,
        NOT_AUTHORIZED,
        INVALID_EMAIL_FORMAT,
        INVALID_TOKEN
    }

    public static String toString(ErrorCodes errorRef) {
        return errorRef.toString();
    }
}
