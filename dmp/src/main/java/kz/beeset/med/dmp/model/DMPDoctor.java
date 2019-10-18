package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dmp_doctor")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPDoctor extends BaseAuditable{
    @Id
    private String id;
    @Indexed
    private String dmpId;
    private String userId;
    private String codeNumber;
    @JsonIgnore
    private int state;
}
