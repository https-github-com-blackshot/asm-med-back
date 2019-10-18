package kz.beeset.med.dmp.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Класс описания форм
 */

@Getter
@Setter
@EqualsAndHashCode
public class Form{
    @Id
    public String id;
    public String nameKz;
    public String nameRu;
    public String nameEn;
    public String descriptionKz;
    public String descriptionRu;
    public String descriptionEn;
    public String typeForm;
    public String subTypeForm;
    public boolean global;
    public String organizationId;
    public String code;
    public String formModuleType;
    public Object tableFormDesign;
    private boolean obligatory;

    /**
     * Тип формы
     * 1 - КИ
     * 2 - скрининг
     */
    public int type;
    public String formTypeId;
    public String moduleId;
    /**
     * Статус
     *  1 Активный
     *  5 Удалено
     */
    @JsonIgnore
    public int state;




}
