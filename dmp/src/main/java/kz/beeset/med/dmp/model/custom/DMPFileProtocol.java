package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.dmp.model.DMPProtocol;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class DMPFileProtocol {

    @Id
    private String id;
    private DMPProtocol dmpProtocol;
    private List<MultipartFile> files;

}
