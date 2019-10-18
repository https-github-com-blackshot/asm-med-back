package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dmp_organization_map")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPOrganizationMap extends BaseAuditable {

    @Id
    private String id;
    private String dmpId;
    private String organizationId;

    @JsonIgnore
    private int state;

}
