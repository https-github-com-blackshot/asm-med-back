package kz.beeset.med.admin.utils.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CloudocIllegalArgumentException extends CloudocException {

    private static final long serialVersionUID = 1L;

    public CloudocIllegalArgumentException(String message) {
        super(message);
    }

    public CloudocIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

}
