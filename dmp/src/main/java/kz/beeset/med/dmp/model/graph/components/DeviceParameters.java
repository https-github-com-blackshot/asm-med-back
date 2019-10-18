package kz.beeset.med.dmp.model.graph.components;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class DeviceParameters {

    private String label;
    private String yAxisID;
    private Integer lineTension;
    private List<Object> data;
    private String fill;

}
