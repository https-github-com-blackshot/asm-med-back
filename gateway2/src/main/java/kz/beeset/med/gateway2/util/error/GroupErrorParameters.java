package kz.beeset.med.gateway2.util.error;

import java.util.LinkedList;
import java.util.List;

public class GroupErrorParameters {

    private String className;
    private String message;
    private List<ErrorParameter> parameters;

    public GroupErrorParameters(String className, String message, List<ErrorParameter> parameters) {
        this.className = className;
        this.message = message;
        this.parameters = parameters;
    }

    public GroupErrorParameters(String className, String message) {
        this.className = className;
        this.message = message;
        this.parameters = new LinkedList<>();
    }

    public void addParameter(String name, String value){
        parameters.add(new ErrorParameter(name, value));
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ErrorParameter> parameters) {
        this.parameters = parameters;
    }

}
