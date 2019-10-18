package kz.beeset.med.admin.model.catalog;

import kz.beeset.med.admin.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "appeal-eventtype")
@Getter
@Setter
@EqualsAndHashCode
public class PAEventType {
    @Id
    private String id;
    private String name;
}
