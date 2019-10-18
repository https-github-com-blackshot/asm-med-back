package kz.beeset.med.gateway2.util.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class TreeDataFolderBased {
    private String label;
    private String data;
    private String parentId;
    private String id;
    private String expandedIcon;
    private String collapsedIcon;
    private Boolean partialSelected;
    private List<TreeDataFolderBased> children;

}
