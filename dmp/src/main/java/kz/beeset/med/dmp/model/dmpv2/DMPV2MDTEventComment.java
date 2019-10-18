package kz.beeset.med.dmp.model.dmpv2;

import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dmpv2_mdt_event_comment")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPV2MDTEventComment extends BaseAuditable {
    @Id
    private String id;
    private String userId;
    private String comment;
    private String fio;
}