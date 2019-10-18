package kz.beeset.med.dmp.model.dmpv2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import kz.beeset.med.dmp.model.DMPMDTEventUpload;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "dmpv2_mdt_event")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPV2MDTEvent extends BaseAuditable {
    @Id
    private String id;
    private String templateId;
    private String diseaseId;
    private String diseaseVisitId;
    private String diseaseUserId;
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
