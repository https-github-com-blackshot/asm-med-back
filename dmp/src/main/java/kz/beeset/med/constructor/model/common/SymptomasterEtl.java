package kz.beeset.med.constructor.model.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SymptomasterEtl {
    private String etlCode;
    private List<SymptomasterEtlValue> value;
}
