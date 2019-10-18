package kz.beeset.med.dmp.model.feign;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class DeviceStatConfig {

    private String id;
    private HashMap<String, DeviceStatConfigObjValue> parameterMap;
    private String userId;

}
