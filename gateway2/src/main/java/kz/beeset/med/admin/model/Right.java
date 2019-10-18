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
import java.util.List;

@Document(collection = "right")
@Getter
@Setter
@EqualsAndHashCode
public class Right {
    @Id
    private String id;
    /**
     * Parent right id
     */
    private String parentId;
    /**
     * Код прав
     */
    private String code;

    /**
     * Наименование на казахском
     */
    private String nameKz;

    /**
     * Наименование на русском
     */
    private String nameRu;

    /**
     * Наименование на английском
     */
    private String nameEn;

    private String type;

    private List<String> resources;

    /**
     * Статус
     * 1 Активный
     * 5 Удалено
     */

    @JsonIgnore
    private int state;

    @JsonIgnore
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

}
