package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Document(collection = "tutorial")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Tutorial extends BaseAuditable {
    @Id
    private String id;
    private String code;
    private String videoLink;
    private String tutorialAvaId;

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
    /**
     * Статус
     * 1 Активный
     * 5 Удалено
     */
    @JsonIgnore
    private int state;
}
