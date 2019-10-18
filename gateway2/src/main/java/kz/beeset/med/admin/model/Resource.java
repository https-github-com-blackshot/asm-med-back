package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "resource")
@Getter
@Setter
@EqualsAndHashCode
public class Resource {
    @Id
    String id;
    String code;
    String resource;
    String type;
    String description;
    String descriptionKz;
    String descriptionEn;
    String parentId;
    Long status;

    @JsonIgnore
    private int state;

    @JsonIgnore
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonIgnore
    @CreatedBy
    private String createdBy;
}
