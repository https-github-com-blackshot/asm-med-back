package kz.beeset.med.device.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;

@Document(collection = "device_stat")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DeviceStat extends BaseAuditable {

    @Id
    private String id;
    private HashMap<String, Object> parameterMap;
    private LocalDateTime checkDate;
    private String deviceName;
    private String userId;

    @JsonIgnore
    private int state;

}
