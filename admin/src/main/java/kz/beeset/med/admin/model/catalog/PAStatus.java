package kz.beeset.med.admin.model.catalog;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "appeal-status")
@Getter
@Setter
@EqualsAndHashCode
public class PAStatus {
    @Id
    private String id;
    private String code;
    private String name;

}
