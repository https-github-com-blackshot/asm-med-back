package kz.beeset.med.dmp.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class User {

    /**
     * Идентификатор
     */
    @Id
    private String id;

    /**
     * Активный ли пользователь
     */
    private boolean active;

    /**
     * Логин
     */
    private String username;

    /**
     * Аватар Id
     */
    private String avatarId;

    /**
     * Электронная почта
     */
    private String email;

    /**
     * ИИН
     */
    private String idn;

    /**
     * Имя
     */
    private String name;

    /**
     * Фамилия
     */
    private String surname;

    /**
     * Отчество
     */
    private String middlename;

    /**
     * Дата рождения
     */
    private LocalDate birthday;

    /**
     * Адрес
     */
    private String address;

    /**
     * Пол
     */
    private String sex;

    /**
     * Мой личный номер телефона(мобильный)
     */
    private String mobilePhone;

    /**
     * Показать пациентам при записи на прием
     */
    private boolean isShowMobilePhone;

    /**
     * Контактные телефоны(видны пациентам при записи на прием)
     */
    private String contactPhone;

    /**
     * Кратко о себе
     */
    private String aboutYourself;

    /**
     * Язык интерфейса
     */
    private String interfaceLang;

    /**
     * Категория
     */
    private String category;

    /**
     * Стаж работы в медицине(в годах)
     */
    private int experience;

    /**
     * Специальность
     */
    private List<String> specialityIds;

    /**
     * Признак пенсионера
     */
    private String pens;

    /**
     * Признак инвалидности
     */
    private String invalidity;

    /**
     * Страховой полис
     */
    private String insurPolicy;

    /**
     * Дата регистрации в системе
     */
    @JsonIgnore
    private LocalDateTime sysRegDate;

    /**
     * Врачи
     */
    private List<String> doctorIds = new ArrayList<String>();


    /**
     * Статус
     *  1 Активный
     *  5 Удалено
     */
    @JsonIgnore
    private int state;

}
