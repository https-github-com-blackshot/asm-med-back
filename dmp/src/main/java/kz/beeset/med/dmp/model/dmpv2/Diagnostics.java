package kz.beeset.med.dmp.model.dmpv2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "dmp_diagnostics")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Diagnostics extends BaseAuditable {

    @Id
    private String id;
    private String code;
    private String name;
    private String description;
    private String categoryId;
    private List<String> diseaseIds;
    private List<String> formIds;

    @JsonIgnore
    private int state;

}
