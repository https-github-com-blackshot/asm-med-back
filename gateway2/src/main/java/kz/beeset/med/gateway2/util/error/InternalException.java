package kz.beeset.med.gateway2.util.error;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class InternalException extends Exception {

    private List<GroupErrorParameters> parameters;
    private Enum errorRef;

    public InternalException(Enum errorRef, List<GroupErrorParameters> parameters, String errMessage) {
        super(errMessage);
        this.errorRef = errorRef;
        this.parameters = parameters;
    }

    public InternalException(Enum errorRef, String errMessage) {
        super(errMessage);
        this.errorRef = errorRef;
        this.parameters = new LinkedList<>();
    }

    public void addGroupErrorParameters(GroupErrorParameters group) {
        getParameters().add(group);
    }

    public List<GroupErrorParameters> getParameters() {
        return parameters;
    }

    public Enum getErrorRef() {
        return errorRef;
    }

    public String getAllErrorsInString() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (GroupErrorParameters group : parameters) {
                baos.write(group.getClassName().getBytes());
                baos.write(". Error: \"".getBytes());
                baos.write(group.getMessage().getBytes());
                baos.write("\" -> ".getBytes());
            }
            baos.write(" END".getBytes());
            return baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "!!! InternalException.getAllErrorsInString: cannot get error !!!";
        }
    }

    public String makeString() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write("error ref: ".getBytes());
            baos.write(errorRef.toString().getBytes());
            baos.write("\n".getBytes());
            for (GroupErrorParameters group : parameters) {
                baos.write("class: ".getBytes());
                baos.write(group.getClassName().getBytes());
                baos.write("\n".getBytes());
                baos.write("error: ".getBytes());
                baos.write(group.getMessage().getBytes());
                baos.write("\n".getBytes());
                for (ErrorParameter parameter : group.getParameters()) {
                    baos.write(parameter.getName().getBytes());
                    baos.write(": ".getBytes());
                    if (parameter.getValue() == null) {
                        baos.write("null".getBytes());
                    } else {
                        baos.write(parameter.getValue().getBytes());
                    }
                    baos.write("\n".getBytes());
                }
            }
            return baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "!!! InternalException.makeString: cannot get result !!!";
        }
    }
}
