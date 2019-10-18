package kz.beeset.med.dmp.firebase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationModel {

    private String title;
    private Object body;
    private String icon;
    private String clickAction;
    private String ttlInSeconds;

}
