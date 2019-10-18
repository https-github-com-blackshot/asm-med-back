package kz.beeset.med.gateway2.util;

import kz.beeset.med.admin.model.Constants;

import java.util.List;

public class SpringPlatformException extends Exception{

    private String etype = Constants.EXCEPTION_TYPE_ERROR;
    private List<String> parameters;

    protected SpringPlatformException(String message, Throwable cause, String etype, List<String> parameters) {
        super(message, cause);
        this.etype = etype;
        this.parameters = parameters;
    }

    public SpringPlatformException(String message, String etype, List<String> parameters) {
        super(message);
        this.etype = etype;
        this.parameters = parameters;
    }

    public SpringPlatformException(String message) {
        super(message);
    }

    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
