package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.DMPPatientAppeal;
import kz.beeset.med.dmp.model.common.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DMPPatientAppealCustom {

    private DMPPatientAppeal dmpPatientAppeal;
    private Object patientUser;

}
