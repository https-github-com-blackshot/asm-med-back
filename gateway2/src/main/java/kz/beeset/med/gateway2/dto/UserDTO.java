package kz.beeset.med.gateway2.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 91901774547107674L;

    private String idn;
    private String signupToken;
    private String password;
    private String email;
    private String mobilePhone;
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
     * Адрес
     */
    private String workplace;

    /**
     * Пол
     */
    private String sex;

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
     * Национальность
     */
    private String nationality;

}
