package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPPatient;
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
public class DMPPatientCustom {

    private DMPPatient dmpPatient;
    private DMP dmp;
    private List<DMPDoctorCustom> dmpDoctorList;
    private User user;
    private int healthStatus;

}
