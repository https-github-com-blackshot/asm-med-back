package kz.beeset.med.dmp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dmp_mdt_event_comment")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPMDTEventComment extends BaseAuditable{
    @Id
    private String id;
    private String userId;
    private String comment;
    private String fio;
}