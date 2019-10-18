package kz.beeset.med.dmp.model.dmpv2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@Document(collection = "dmpv2_notifications")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPV2Notifications extends BaseAuditable {

    @Id
    private String id;
    private List<String> patients;
    private String type;
    private String message;
    private LocalDate beginDate;
    private LocalDate endDate;
    private HashMap<String, Boolean> period;
    private LocalTime eventTime;
    private Boolean toAll;

    private String profileId;

    private String doctorUserId;
    private String patientUserId;

    @JsonIgnore
    private int state;

}
