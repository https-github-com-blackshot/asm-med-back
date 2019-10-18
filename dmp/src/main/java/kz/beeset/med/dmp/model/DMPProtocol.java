package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "dmp_protocol")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPProtocol extends BaseAuditable{

    @Id
    private String id;
    private String code;
    private String dmpId;
    private String name;
    private String description;
    private Map<String, String> fileIds;

    @JsonIgnore
    private int state;
}
