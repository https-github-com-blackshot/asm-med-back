package kz.beeset.med.admin.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "session")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Session {

    @Id
    private String id;
    private User user;
    private String token;
    private String tokenCreateDate;
    private String tokenExpireDate;
    private String selectedOrganizationId;
}
