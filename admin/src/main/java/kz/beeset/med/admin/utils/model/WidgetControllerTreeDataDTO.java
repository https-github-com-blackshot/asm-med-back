package kz.beeset.med.admin.utils.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class WidgetControllerTreeDataDTO {
    private Object data;
    private List<?> children;
}
