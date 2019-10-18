package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "dmp_visit")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPVisit extends BaseAuditable{
    @Id
    private String id;
    @Indexed
    private String dmpId;
    private String code;
    private String nameKz;
    private String nameRu;
    private String nameEn;
    private String description;
    private String dmpScheduleTypeId;
    private int days;
    private int deadlineOffset;
    private List<String> forms = new ArrayList<String>();
    @JsonIgnore
    private int state;
}
