package kz.beeset.med.admin.utils.error;

public class CloudocException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CloudocException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudocException(String message) {
        super(message);
    }

}
