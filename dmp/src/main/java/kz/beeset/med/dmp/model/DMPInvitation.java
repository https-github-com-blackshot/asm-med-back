package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dmp_invitation")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPInvitation extends BaseAuditable{

    @Id
    private String id;
    private String dmpId;
    private String dmpDoctorId;
    private String dmpDoctorUserId;
    private String patientUserId;
    private String message;
    private Integer invitationStatus;

    @JsonIgnore
    private int state;

}
