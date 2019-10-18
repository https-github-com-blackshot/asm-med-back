package kz.beeset.med.dmp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DMPMDTEventUpload {
    private String code;
    private String name;
    private String description;
    private List<String> viewers;
    private List<DMPMDTEventUploadFile> files;
    private List<String> comments;
}
