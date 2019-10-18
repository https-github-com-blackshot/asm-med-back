package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPInvitation;
import kz.beeset.med.dmp.model.common.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DMPInvitationCustom {

    private DMPInvitation dmpInvitation;
    private DMP dmp;
    private User patientUser;

}
