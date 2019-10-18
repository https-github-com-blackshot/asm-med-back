package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.dmpv2.DMPV2MDTEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DMPV2MDTEventCustom {
    private DMPV2MDTEvent event;
    private User user;
}
