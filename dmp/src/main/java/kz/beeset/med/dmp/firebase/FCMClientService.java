package kz.beeset.med.dmp.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.service.IDMPService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FCMClientService {

    @Autowired
    private IDMPService dmpService;

    public static final String serviceAccountFile = "dmp\\src\\main\\java\\kz\\beeset\\med\\dmp\\utils\\firebase\\cloudoc-9d1b9-firebase-adminsdk-krmtd-1bad4db4dc.json";

    public FCMClientService(FCMSettings settings) {
        try {
            FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://cloudoc-9d1b9.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendByTopic(PushNotificationModel model, String dmpId) throws InterruptedException, ExecutionException, InternalException {
        DMP dmp = dmpService.get(dmpId);

        Message message = Message.builder().setTopic(dmp.getNameRu())
                .setWebpushConfig(WebpushConfig.builder()
                        .putHeader("ttl", model.getTtlInSeconds())
                        .setNotification(createBuilder(model).build())
                        .build())
                .build();

        String response = FirebaseMessaging.getInstance()
                .sendAsync(message).get();

        return response;
    }

    public String sendPersonal(PushNotificationModel model, String clientToken)
            throws ExecutionException, InterruptedException {
        Message message = Message.builder().setToken(clientToken)
                .setWebpushConfig(WebpushConfig.builder()
                        .putHeader("ttl", model.getTtlInSeconds())
                        .setNotification(createBuilder(model).build())
                        .build())
                .build();

        String response = FirebaseMessaging.getInstance()
                .sendAsync(message)
                .get();
        return response;
    }


    public void subscribeUsers(String dmpId, List<String> clientTokens) throws InternalException, FirebaseMessagingException {
        DMP dmp = dmpService.get(dmpId);

        for (String token : clientTokens) {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(Collections.singletonList(token), dmp.getNameRu());

        }

    }

    public TopicManagementResponse subscribeUser(String dmpId, String token) throws InternalException, FirebaseMessagingException {
        DMP dmp = dmpService.get(dmpId);
        return FirebaseMessaging.getInstance().subscribeToTopic(Collections.singletonList(token), dmp.getCode());
    }

    public WebpushNotification.Builder createBuilder(PushNotificationModel model) {
        WebpushNotification.Builder builder = WebpushNotification.builder();
        builder.addAction(new WebpushNotification
                .Action(model.getClickAction(), "Открыть"))
                .setImage(model.getIcon())
                .setTitle(model.getTitle())
                .setData(model.getBody());
        return builder;
    }

}
