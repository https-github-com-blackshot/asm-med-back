package kz.beeset.med.admin.utils.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CloudocConflictException extends CloudocException {

    private static final long serialVersionUID = 1L;

    public CloudocConflictException(String message) {
        super(message);
    }
}