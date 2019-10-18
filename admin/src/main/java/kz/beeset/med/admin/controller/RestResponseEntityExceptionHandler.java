package kz.beeset.med.admin.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.beeset.med.admin.utils.strategy.AnnotationExclusionStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    public final String SUCCESS = "success", ERROR = "error";

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
            .setExclusionStrategies(new AnnotationExclusionStrategy()).create();

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {

        Map answer = new HashMap<>();
        answer.put("status", SUCCESS);
        answer.put("data", "Access denied");

        String response = GSON.toJson(answer);

        return new ResponseEntity<Object>(
                response, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
}
