package kz.beeset.med.dmp.utils.model;

import java.util.List;

public class TreeData {

    private Object data;
    private boolean expanded;
    private List<?> children;

    public TreeData() {
    }

    public TreeData(Object data, boolean expanded, List<?> children) {
        this.data = data;
        this.expanded = expanded;
        this.children = children;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

}
