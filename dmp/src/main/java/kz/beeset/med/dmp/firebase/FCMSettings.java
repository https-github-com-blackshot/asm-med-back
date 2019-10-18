package kz.beeset.med.dmp.firebase;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "fcm")
@Component
@Getter
@Setter
public class FCMSettings {

    public static final String serviceAccountFile = "/kz/beeset/med/dmp/utils/firebase/cloudoc-9d1b9-firebase-adminsdk-krmtd-1bad4db4dc.json";

}
