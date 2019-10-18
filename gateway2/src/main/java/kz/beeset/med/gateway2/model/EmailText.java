package kz.beeset.med.gateway2.model;

import kz.beeset.med.gateway2.model.base.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "email_text")
@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
public class EmailText extends BaseEntity {

    private String code;
    private String headerRu;
    private String headerEn;
    private String headerKz;
    private String bodyRu;
    private String bodyKz;
    private String bodyEn;
}
