package kz.beeset.med.constructor.model.guide;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.constructor.model.common.FormSymptomaster;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Класс описания форм
 */

@Document(collection = "fdc_form")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Form extends BaseAuditable {
    @Id
    private String id;
    private String nameKz;
    private String nameRu;
    private String nameEn;
    private String descriptionKz;
    private String descriptionRu;
    private String descriptionEn;
    private String typeForm;
    private String subTypeForm;
    private boolean global;
    private String organizationId;
    private String code;
    private String formModuleType;
    private boolean obligatory;
    private Object tableFormDesign;
    private Object section;
    private boolean useSection;
    private Object formBRule;
    private FormSymptomaster formSymptomaster[];

    /**
     * Тип формы
     * 1 - КИ
     * 2 - скрининг
     */
    private int type;
    private String formTypeId;

    /**
     * ID КИ,ПУЗ в которой используется данная форма.
     * для глобальных БП
     */
    private String moduleId;

    /**
     * Статус
     *  1 Активный
     *  5 Удалено
     */
    @JsonIgnore
    private int state;




}
