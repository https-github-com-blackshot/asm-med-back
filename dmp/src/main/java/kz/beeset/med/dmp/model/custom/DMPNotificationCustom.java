package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.DMPNotification;
import kz.beeset.med.dmp.model.common.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DMPNotificationCustom {

    private DMPNotification dmpNotification;
    private List<User> patients;

}
