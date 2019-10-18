package kz.beeset.med.admin.model.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class MenuTree {
    private String id;
    private String label;
    private String data;
    private String expandedIcon;
    private String collapsedIcon;
    private String parentId;
    private List<MenuTree> children;

}
