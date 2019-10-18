package kz.beeset.med.dmp.model.custom;


import kz.beeset.med.dmp.model.DMPMDTEvent;
import kz.beeset.med.dmp.model.common.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DMPMDTEventCustom {
    private DMPMDTEvent event;
    private User user;
}
