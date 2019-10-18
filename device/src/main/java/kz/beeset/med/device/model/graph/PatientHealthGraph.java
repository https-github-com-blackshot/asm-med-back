package kz.beeset.med.device.model.graph;

import kz.beeset.med.device.model.graph.components.Color;
import kz.beeset.med.device.model.graph.components.DeviceParameters;
import kz.beeset.med.device.model.graph.components.Options;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class PatientHealthGraph {

    private String chartType;
    private LinkedHashMap<String, List<DeviceParameters>> datasets;
    private LinkedHashMap<String, List<String>> labels;
    private List<Color> colors;
    private Options options;

}
