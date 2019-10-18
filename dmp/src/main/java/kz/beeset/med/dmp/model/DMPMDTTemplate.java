package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "dmp_mdt_template")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPMDTTemplate extends BaseAuditable {
    @Id
    private String id;
    private String name;
    private String dmpId;
    private List<String> doctors;
    //Owner - doctor user id
    private String ownerId;
    @JsonIgnore
    private int state;
}
