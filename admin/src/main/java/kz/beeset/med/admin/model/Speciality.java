package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "specialities")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Speciality extends BaseAuditable {

    /**
     * Идентификатор
     */
    @Id
    private String id;

    /**
     * Код специальности
     */
    private String code;

    /**
     * Наименование
     */
    private String name;

    /**
     * Среднее время приема(мин.)
     */
    private int receptionTimeInMinute;

    /**
     * Участковый
     */
    private boolean precinct;

    /**
     * Статус
     *  1 Активный
     *  5 Удалено
     */
    @JsonIgnore
    private int state;

    public Speciality() {

    }

    public Speciality(String id, String code, String name, int receptionTimeInMinute, boolean precinct) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.receptionTimeInMinute = receptionTimeInMinute;
        this.precinct = precinct;
    }
}
