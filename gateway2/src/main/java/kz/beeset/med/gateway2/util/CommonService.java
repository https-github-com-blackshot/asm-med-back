package kz.beeset.med.gateway2.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.beeset.med.gateway2.util.strategy.AnnotationExclusionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
            .setExclusionStrategies(new AnnotationExclusionStrategy()).create();
    private static final Gson GSON_DATETIME = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls()
            .setExclusionStrategies(new AnnotationExclusionStrategy()).create();
    private static final Gson GSON_DATE_ONLY = new GsonBuilder().setDateFormat("yyyy-MM-dd").serializeNulls()
            .setExclusionStrategies(new AnnotationExclusionStrategy()).create();
//    InternalExceptionHelper ieHelper = new InternalExceptionHelper(this.getClass().getCanonicalName());
    //public static EsuvoResponse.ResponseBuilder builder = EsuvoResponse.status(EsuvoResponse.Status.OK);
    public final String SUCCESS = "success", ERROR = "error";
    protected String locale;
    protected Long brand;

    protected ResponseEntity<?> builder() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected ResponseEntity<?> builder(Object object) {
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    protected ResponseEntity<?> builder(Object object, HttpStatus httpStatus) {
        return new ResponseEntity<>(object, httpStatus);
    }

//    public void setSessionData(String sessionRef) throws InternalException {
//        try {
//            if (sessionRef != null && !sessionRef.isEmpty()) {
//                Session session = authService.getSession(sessionRef);
//                LOGGER.debug("locale: " + session.getLocale());
//
//                this.locale = session.getLocale();
//                this.brand = session.getBrand();
//            }
//        } catch (InternalException ex) {
//            this.locale = defaultLocale;
//        }
//        try {
//            checkManagerSession(sessionRef);
//        } catch (InternalException ie) {
//            throw ie;
//        }
//    }

//    public void setSessionData(Session session) throws InternalException {
//        this.locale = session.getLocale();
//        this.brand = session.getBrand();
//
//        try {
//            checkManagerSession(session.getReference());
//        } catch (InternalException ie) {
//            throw ie;
//        }
//    }

//    private void checkManagerSession(String clientSessionRef) throws InternalException {
//        try {
//            if (clientSessionRef != null && !clientSessionRef.isEmpty()) {
//                Session session = authService.getSession(clientSessionRef);
//                Long managerSessionId = session.getManagerSessionId();
//                if (managerSessionId != null) {
//                    // client session is opened by lo manager
//                    // check lo manager session
//                    LOGGER.debug("check LO_Manager session for id: " + managerSessionId);
//                    ManagerSession managerSession = authService.getManagerSessionById(managerSessionId);
//                    managerSession = authService.checkManagerSession(managerSession.getReference(), true);
//                    LOGGER.debug("LO_Manager session checked reference: " + managerSession.getReference());
//                }
//            }
//        } catch (InternalException ie) {
//            throw ie;
//        } catch (Exception e) {
//            throw ieHelper.generate(ServiceSystemEM.ErrorRef.SYSTEM_ERROR, e.getMessage(), e,
//                    new Pair("clientSessionRef", clientSessionRef));
//        }
//    }

    public String getLocale() {
        if (locale == null) {
            this.locale = "ru";
        }
        return this.locale;
    }


    public String success() {
        Map answer = new HashMap();
        answer.put("status", SUCCESS);
        String response = GSON.toJson(answer);

        return response;
    }

    public String success(Object data) {
        Map answer = new HashMap<>();
        answer.put("status", SUCCESS);
        answer.put("data", data);

        String response = GSON.toJson(answer);

        return response;
    }

    public String successDateTime(Object data) {
        Map answer = new HashMap<>();
        answer.put("status", SUCCESS);
        answer.put("data", data);

        String response = GSON_DATETIME.toJson(answer);

        return response;
    }

    public String successDateOnly(Object data) {
        Map answer = new HashMap<>();
        answer.put("status", SUCCESS);
        answer.put("data", data);

        String response = GSON_DATE_ONLY.toJson(answer);

        return response;
    }

    public String error(Enum errorRef) {
        String response = GSON.toJson(getErrorMap(errorRef));
        LOGGER.error(response);

        return response;
    }

    public String errorWithData(Enum errorRef, Object data) {
        Map answer = getErrorMap(errorRef);
        answer.put("data", data);

        String response = GSON.toJson(answer);
        LOGGER.error(response);

        return response;
    }

    public String errorWithDescription(Enum errorRef, String errorDesc) {
        Map answer = getErrorMap(errorRef);
        answer.put("error_description", errorDesc);

        String response = GSON.toJson(answer);
        LOGGER.error(response);

        return response;
    }

    public String errorWithDataAndDescription(Enum errorRef, String errorDesc, Object data) {
        Map answer = getErrorMap(errorRef);
        answer.put("error_description", errorDesc);
        answer.put("data", data);

        String response = GSON.toJson(answer);
        LOGGER.error(response);

        return response;
    }

    private Map getErrorMap(Enum errorRef) {
        String errorCode = errorRef != null ? errorRef.toString() : "SYSTEM_ERROR";

        Map answer = new HashMap<>();
        answer.put("status", ERROR);
        answer.put("error_code", errorCode);
        answer.put("error_description", getErrorDescription(errorCode));

        return answer;
    }

    private String getErrorDescription(String errorCode) {
        errorCode = errorCode != null ? errorCode : "SYSTEM_ERROR";
        String errorKey = getContextErrorCode(errorCode);
//        try {
//            if (dict.isExist(errorKey, getLocale())) {
//                messagesBean.updateErrorMessageIfExist(errorKey, getLocale(), true);
//                return dict.getTranslate(errorKey, getLocale());
//            } else {
//                messagesBean.saveNotTranslatedErrorCodes(errorKey, getLocale());
//                return errorKey;
//            }
//        } catch (Exception e) {
//            LOGGER.error("Error while get translate for " + errorKey);
//            return errorKey;
//        }
        return errorKey;
    }

    public String getErrorDescription(Enum errorCode) {
        return getErrorDescription(errorCode != null ? errorCode.toString() : "SYSTEM_ERROR");
    }

    private String getContextErrorCode(String errorCode) {
        switch (errorCode) {
            case "SYSTEM_ERROR":
            case "SESSION_EXPIRED_OR_CLOSED":
                return String.format("REST_ERROR-%s", errorCode);
            default:
                return String.format("REST_ERROR-%s_%s", this.getClass().getSimpleName().toLowerCase(), errorCode);
        }
    }
}
