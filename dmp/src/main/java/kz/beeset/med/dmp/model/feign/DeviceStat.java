package kz.beeset.med.dmp.model.feign;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
public class DeviceStat {

    private String id;
    private HashMap<String, Object> parameterMap;
    private LocalDateTime checkDate;
    private String deviceName;
    private String userId;

}
