package kz.beeset.med.constructor.model.guide;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Класс описания строк и столбцов
 */

@Document(collection = "fdc_form_field")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FormField extends BaseAuditable {
    @Id
    private String id;

    private String formId;
    private String fieldType;

    private String nameKz;
    private String nameRu;
    private String nameEn;
    private String descriptionKz;
    private String descriptionRu;
    private String descriptionEn;

    private String code;

    private String choiceListId;
    private String gridFormId;

    //------------------------
    // --- NON USE ---
    private Object defaultVal;
    private int fieldSize;
    private int maxValue;
    private int minValue;
    private Date minDateValue;
    private Date maxDateValue;

    //  field style
    private boolean labelVisible = false;
    private int labelFontSize = 18;

    /**
     * Тип строки
     * 101 - Заголовочная строка
     * 102 - Дочерняя строка
     */
    private int rowType;

    /**
     * Путь (Разделитель ">")
     */
    private String path;
    // --- NON USE ---
    //------------------------

    private boolean required;
    private boolean visible;

    private boolean gridField;
    private String parentFieldId;
    private int gridObjsCount;
    private Object hideConditions;

    private boolean systemField;

    @JsonIgnore
    private int state;
}
