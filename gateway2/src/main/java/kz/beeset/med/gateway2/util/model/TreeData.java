package kz.beeset.med.gateway2.util.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class TreeData {

    private Object data;
    private boolean expanded;
    private List<?> children;

}
