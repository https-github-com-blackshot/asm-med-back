package kz.beeset.med.dmp.model.dmpv2;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DMPV2VisitMeasurement {
    private Double height;
    private Double weight;
    private Double bmi;
    private List<Integer> adSys;
    private List<Integer> adDys;
    private String ad;
}
