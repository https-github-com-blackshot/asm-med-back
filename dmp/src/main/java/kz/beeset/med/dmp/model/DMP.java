package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "dmp")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMP extends BaseAuditable{
    @Id
    private String id;
    private String code;
    private String nameKz;
    private String nameRu;
    private String nameEn;
    private String description;
    private String category;
    private LocalDate registrationDate;
    private List<String> scheduleTypeIds = new ArrayList<String>();
    @JsonIgnore
    private int state;
}
