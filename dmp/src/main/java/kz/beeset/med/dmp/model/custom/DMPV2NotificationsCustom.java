package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.dmpv2.DMPV2Notifications;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DMPV2NotificationsCustom {

    private DMPV2Notifications dmpv2Notification;
    private List<User> patients;

}
