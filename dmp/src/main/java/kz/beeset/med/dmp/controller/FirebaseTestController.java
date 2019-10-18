package kz.beeset.med.dmp.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import kz.beeset.med.dmp.firebase.FCMClientService;
import kz.beeset.med.dmp.firebase.PushNotificationModel;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("dmp/firebase/test")
@Api(tags = {"FIREBASE"}, description = "FIREBASE", authorizations = {@Authorization(value = "bearerAuth")})
public class FirebaseTestController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPAdminController.class);

    @Autowired
    private FCMClientService fcmClientService;

    @ApiOperation(value = "Подписка пользователя к уведолмения по пузам", tags = {"FIREBASE"})
    @RequestMapping(value = "subscribe/users/{dmpId}/{token}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> subscribeUsers(@PathVariable String dmpId, @PathVariable String token) {
        try {
            return builder(success(fcmClientService.subscribeUser(dmpId, token)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        } catch (FirebaseMessagingException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(null, e.getMessage()));
        }
    }

    @ApiOperation(value = "Отправка пуш уведомления пользователю с данным токеном", tags = {"FIREBASE"})
    @RequestMapping(value = "send/notification/{token}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> subscribeUsers(@PathVariable String token,
                                            @Valid @RequestBody PushNotificationModel model) {
        try {
            return builder(success(fcmClientService.sendPersonal(model, token)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
