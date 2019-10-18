package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;

@Document(collection = "dmp_device_stat")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPDeviceStat extends BaseAuditable {
    @Id
    private String id;
    private HashMap<String, Object> parameterMap;
    // Device - data checking date and time
    private LocalDateTime checkDate;
    private String deviceInfo;
    private String dmpId;
    private String dmpPatientId;
    private String dmpPatientUserId;
    @JsonIgnore
    private int state;
}
