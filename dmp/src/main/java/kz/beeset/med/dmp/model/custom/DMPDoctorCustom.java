package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.common.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DMPDoctorCustom {

    private DMPDoctor dmpDoctor;
    private User user;

}
