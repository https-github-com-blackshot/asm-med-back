package kz.beeset.med.dmp.model.dmpv2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "dmpv2_mdt_template")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPV2MDTTemplate extends BaseAuditable {
    @Id
    private String id;
    private String name;
    private String diseaseId;
    private List<String> doctors;
    //Owner - doctor user id
    private String ownerId;

    @JsonIgnore
    private int state;
}
