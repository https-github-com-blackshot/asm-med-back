package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "resource")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Resource extends BaseAuditable {
    @Id
    String id;
    String code;
    String resource;
    /**
     * Тип ресурса
     *  1 - контроллеры
     *  2 - кнопки
     *  3 - меню
     */
    String type;
    String icon;
    String description;
    String descriptionRu;
    String descriptionKz;
    String descriptionEn;
    String moduleType;
    String parentId;
    /**
     * Статус ресурса
     * 1 - активный
     * 2 - не активный
     */
    Long status;

    @JsonIgnore
    private int state;

}
