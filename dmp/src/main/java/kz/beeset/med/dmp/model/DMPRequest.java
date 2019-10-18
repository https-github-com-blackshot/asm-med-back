package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Document(collection = "dmp_request")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPRequest extends BaseAuditable {

    @Id
    private String id;
    private String fio;
    private String idn;
    private String phone;
    private String email;
    private String userId;
    private String comments;
    private List<String> dmpIds;
    private Map<String, String> fileIds;

    @CreatedDate
    private LocalDate applyDate;

    private int state;

}
