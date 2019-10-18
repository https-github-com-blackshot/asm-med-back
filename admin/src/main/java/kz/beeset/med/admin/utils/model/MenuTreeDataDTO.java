package kz.beeset.med.admin.utils.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class MenuTreeDataDTO {
    private String id;
    private String title;
    private String translate;
    private String type;
    private String icon;
    private String url;
    private List<?> children;
}
