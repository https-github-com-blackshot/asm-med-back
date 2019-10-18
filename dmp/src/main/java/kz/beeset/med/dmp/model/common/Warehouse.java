package kz.beeset.med.dmp.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class Warehouse{
    @Id
    private String id;

    private Object data;

    @Indexed
    private String formId;

    @Indexed
    private String formCode;

    private boolean obligatory;

    private String parentVisitId;

    @Indexed
    private String userId;

    @Indexed
    private String kiId;

    @Indexed
    private String visitId;

    private boolean checkFullFill;

    @JsonIgnore
    private LocalDateTime checkFullFillDate;

    @JsonIgnore
    private int state;

    private Integer exclusion;
}
