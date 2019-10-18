package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.Map;

@Document(collection = "dmp_patient_appeal_comment")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DMPPatientAppealComment extends BaseAuditable {

    @Id
    private String id;
    @Indexed
    private String dmpPatientAppealId;
    private String userId;
    private String note;
    private String roleCode;

    /*
     * Вложения по комментам обращении пациента
     * */
    private Map<String, String> attachments;

    @JsonIgnore
    private int state;

}
