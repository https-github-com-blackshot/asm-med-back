package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
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
     * Пароль
     */
    @JsonIgnore
    private String password;

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
     * Место работы
     */
    private String workplace;

    /**
     * Должность
     */
    private String job;

    /**
     * Рост
     */
    private String height;

    /**
     * Вес
     */
    private String weight;

    /**
     * Цвет
     */
    private String color;

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
    private int experiance;

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
     * Состояние здоровья
     */
    private Integer healthStatus;

    /**
     * Дата регистрации в системе
     */
    @JsonIgnore
    private LocalDateTime sysRegDate;

    private List<UserRoleOrgMap> userRoleOrgMapList = new ArrayList<UserRoleOrgMap>();
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

    @JsonIgnore
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @Transient
    private Integer age;

    @Transient
    private String photoUrl;

}
