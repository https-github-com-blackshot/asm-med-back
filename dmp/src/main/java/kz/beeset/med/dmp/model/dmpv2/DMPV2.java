package kz.beeset.med.dmp.model.dmpv2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "dmp-v2")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPV2 extends BaseAuditable {
    @Id
    private String id;
    private DiseaseData diseaseData;
    private LocalDateTime nextVisitDate;
    private Boolean checkFullFill;
    private List<String> selectedDiseaseIds;
    private List<String> selectedDiagnosticIds;
    private List<String> selectedLaboratoryIds;
    private List<String> selectedMedicineIds;
    private List<String> selectedProceduresAndInterventionsIds;
    private Object symptomasterValue;

    @JsonIgnore
    private int state;
}
