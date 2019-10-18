package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "dmp_mdt_event")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPMDTEvent extends BaseAuditable {
    @Id
    private String id;
    private String templateId;
    private String dmpId;
    private String dmpVisitId;
    private String dmpPatientId;
    //pendingDoctorUserIdList - doctors that request pending
    private List<String> pendingDoctorUserIdList;
    //acceptedDoctorUserIdList - doctors who accepted to participate in this event
    private List<String> acceptedDoctorUserIdList;
    private String description;
    //owner - doctor user id
    private String ownerId;
    private List<DMPMDTEventUpload> eventUploadList;
    @JsonIgnore
    private int state;
}
