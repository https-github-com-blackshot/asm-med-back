package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "dmp_patient_appeal")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DMPPatientAppeal extends BaseAuditable{

    @Id
    private String id;

    /*
    * Вложения по обращениям пациента
    * */
    private Map<String, String> attachments;
    private String title;
    private String note;
    private boolean messageForAll;
    private String dmpPatientId;
    private String dmpDoctorId;
    private String dmpId;

    /*
     * ПУЗ, КИ
     * */
    private String projectCode;
    private LocalDateTime eventDate;

    /*
     * evet type (проведена операция, попал в болницу итд)
     * */
    private String eventType;
    private String status;

    @JsonIgnore
    private int state;

    @Transient
    private List<String> doctorIds;

}
