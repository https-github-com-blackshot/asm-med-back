package kz.beeset.med.admin.utils.error;

public class CloudocObjectNotFoundException extends CloudocException {

    private static final long serialVersionUID = 1L;

    private Class<?> objectClass;

    public CloudocObjectNotFoundException(String message) {
        super(message);
    }

    public CloudocObjectNotFoundException(String message, Class<?> objectClass) {
        this(message, objectClass, null);
    }

    public CloudocObjectNotFoundException(Class<?> objectClass) {
        this(null, objectClass, null);
    }

    public CloudocObjectNotFoundException(String message, Class<?> objectClass, Throwable cause) {
        super(message, cause);
        this.objectClass = objectClass;
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

}
