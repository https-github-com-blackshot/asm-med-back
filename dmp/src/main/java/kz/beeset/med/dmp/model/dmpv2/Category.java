package kz.beeset.med.dmp.model.dmpv2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dmp_category")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseAuditable {

    @Id
    private String id;
    private String code;
    private String name;
    private String description;
    private String filter;

    @JsonIgnore
    private int state;

}
