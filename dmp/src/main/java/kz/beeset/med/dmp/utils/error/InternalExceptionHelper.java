package kz.beeset.med.dmp.utils.error;

import kz.beeset.med.dmp.utils.MySerialization;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class InternalExceptionHelper {

    private final String CLASS_NAME;

    public InternalExceptionHelper(String className) {
        this.CLASS_NAME = className;
    }

    public InternalException generate(Enum errorRef, String message, Pair... params) {
        GroupErrorParameters groupParams = new GroupErrorParameters(CLASS_NAME, message);
        for (Pair param : params) {
            groupParams.addParameter(param.getFirst(), MySerialization.OToXml(param.getSecond(), null));
        }

        InternalException ie = new InternalException(errorRef, message);
        ie.addGroupErrorParameters(groupParams);

        return ie;
    }
    public InternalException generate(Enum errorRef, String message, Exception e, Pair... params) {

        GroupErrorParameters groupParams = new GroupErrorParameters(CLASS_NAME, message);
        for (int i = 0; i < params.length; i++) {
            groupParams.addParameter(params[i].getFirst(), MySerialization.OToXml(params[i].getSecond(), null));
        }
        groupParams.addParameter("exception", MySerialization.exceptionToString(e));

        InternalException ie = new InternalException(errorRef, message);
        ie.addGroupErrorParameters(groupParams);

        return ie;

    }

    public InternalException generateSystemError(String message, Exception e, Pair... params) {

        GroupErrorParameters groupParams = new GroupErrorParameters(CLASS_NAME, message);
        for (int i = 0; i < params.length; i++) {
            groupParams.addParameter(params[i].getFirst(), MySerialization.OToXml(params[i].getSecond(), null));
        }
        groupParams.addParameter("exception", MySerialization.exceptionToString(e));

        InternalException ie = new InternalException(ErrorCode.ErrorCodes.SYSTEM_ERROR, message);
        ie.addGroupErrorParameters(groupParams);

        return ie;

    }

    public InternalException generate(Enum errorRef, String message, Exception e, List<Pair> params) {

        GroupErrorParameters groupParams = new GroupErrorParameters(CLASS_NAME, message);
        for (Pair param : params) {
            groupParams.addParameter(param.getFirst(), MySerialization.OToXml(param.getSecond(), null));
        }
        groupParams.addParameter("exception", MySerialization.exceptionToString(e));

        InternalException ie = new InternalException(errorRef, message);
        ie.addGroupErrorParameters(groupParams);

        return ie;

    }

    public void addGroup(InternalException ie, String className, String message, Pair... params) {

        GroupErrorParameters groupParams = new GroupErrorParameters(className, message);
        for (int i = 0; i < params.length; i++) {
            groupParams.addParameter(params[i].getFirst(), MySerialization.OToXml(params[i].getSecond(), null));
        }

        ie.addGroupErrorParameters(groupParams);

    }

    public void addGroup(InternalException ie, String className, String message, List<Pair> params) {

        GroupErrorParameters groupParams = new GroupErrorParameters(className, message);
        for (Pair param : params) {
            groupParams.addParameter(param.getFirst(), MySerialization.OToXml(param.getSecond(), null));
        }

        ie.addGroupErrorParameters(groupParams);

    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }
}
