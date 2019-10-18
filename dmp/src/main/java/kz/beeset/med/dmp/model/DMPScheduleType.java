package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dmp_schedule_type")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DMPScheduleType extends BaseAuditable {
    @Id
    private String id;
    private String dmpId;
    private String code;
    private String nameKz;
    private String nameRu;
    private String nameEn;
    private String type;
    private Integer visitCount;
    @JsonIgnore
    private int state;
}
