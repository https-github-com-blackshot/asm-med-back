package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "dmp_notification")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPNotification extends BaseAuditable{
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

    private String dmpDoctorId;
    private String dmpDoctorUserId;
    private String dmpId;

    @JsonIgnore
    private int state;
}
