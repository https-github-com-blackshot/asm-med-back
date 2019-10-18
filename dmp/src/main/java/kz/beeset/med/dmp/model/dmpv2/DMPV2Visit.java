package kz.beeset.med.dmp.model.dmpv2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "dmp_v2_visit")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPV2Visit extends BaseAuditable {

    @Id
    private String id;
    private String nameKz;
    private String nameRu;
    private String nameEn;
    private String description;
    private String dmpV2Id;
    private String patientId;
    private String doctorId;
    private DMPV2VisitMeasurement measurement;
    private LocalDateTime visitDate;
    private String prevVisitId;
    private Integer counter;

    @JsonIgnore
    private int state;
}