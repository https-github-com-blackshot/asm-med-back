package kz.beeset.med.constructor.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "fdc_warehouse")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@CompoundIndexes({
        @CompoundIndex(name = "formId_userId_visitId", def = "{'formId' : 1, 'userId': 1, 'visitId':1}"),
        @CompoundIndex(name = "kiId_userId_visitId", def = "{'kiId' : 1, 'userId': 1, 'visitId':1}"),
        @CompoundIndex(name = "dmpId_userId_visitId", def = "{'dmpId' : 1, 'userId': 1, 'visitId':1}"),
})
public class Warehouse extends BaseAuditable {

    @Id
    private String id;

    private Object data;

    @Indexed
    private String formId;

    @Indexed
    private String formCode;

    private String parentVisitId;

    private boolean obligatory;

    @Indexed
    private String userId;

    @Indexed
    private String kiId;

    @Indexed
    private String dmpId;

    @Indexed
    private String visitId;

    private boolean checkFullFill;

    @JsonIgnore
    private LocalDateTime checkFullFillDate;

    @JsonIgnore
    private int state;

    public boolean getCheck() {
        return checkFullFill;
    }

    public LocalDateTime getCheckDate() {
        return checkFullFillDate;
    }
}
