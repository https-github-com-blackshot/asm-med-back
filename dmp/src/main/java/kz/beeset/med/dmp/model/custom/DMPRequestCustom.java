package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DMPRequestCustom {

    private DMPRequest dmpRequest;
    private List<DMP> dmpList;

}
