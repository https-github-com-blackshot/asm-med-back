package kz.beeset.med.constructor.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormSymptomaster {
    private String generalFieldCode;
    private SymptomasterEtl mainEtl;
    private SymptomasterEtl symptomsEtl;
    private SymptomasterEtl resultEtl;
    private String resultGridFormId;
    private String resultFormCode;
    private int resultGridColOrder;
}
