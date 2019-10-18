package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "organization")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Organization extends BaseAuditable {

    /**
     * Идентификатор
     */
    @Id
    private String id;

    /**
     * id родителя
     */
    @Indexed
    private String parentId;

    /**
     * Наименование организации на казахском языке
     */
    private String nameKz;

    /**
     * Наименование организации на русском языке
     */
    private String nameRu;

    /**
     * Наименование организации на английском языке
     */
    private String nameEn;

    /**
     * Руководитель организации
     */
    @JsonIgnore
    private String head;

    /**
     * Код организации
     */
    private String code;


    /**
     * Город или регион
     */
    private String area;

    /**
     * Адрес
     */
    private String address;

    /**
     * Бик банка, в котором обслуживается данное юр.лицо
     */
    private String bik;

    /**
     * Счёт в банке этого юр.лица
     */
    private String acc;

    /**
     * Контактный телефон
     */
    private String phone;

    @Indexed
    @JsonIgnore
    private String path;

    @JsonIgnore
    private String newPath;

    /**
     * Состояние 1 - норм, 5 - удален, 0 - архивирован и т.д.
     */
    @JsonIgnore
    private int state;

    /**
     *
     */
    @JsonIgnore
    private String tger;

    //private String transaction;

    /**
     * Поле для двухфазного коммита
     *   -1 - начата транзакция вставки
     *   -2 - начата транзакция изменения
     *   -3 - начата транзакция удаления
     * -100 - ошибка во время транзакции вставки
     * -200 - ошибка во время транзакции изменения
     * -300 - ошибка во время транзакции удаления
     *    0 - не активная запись, успешно удаленная
     *    1 - активная запись, успешно вставленная/измененная
     */
    @JsonIgnore
    private int isActive;

    @Transient
    private List<Organization> children;

    /**
     * для JPA обязателен пустой конструктор
     */
    public Organization() {
        // по умолчанию делаем значение -1 для двухфазного коммита
//        this.isActive = -1;
    }

    public Organization(
                   String id,
                   String parentId,
                   String nameKz,
                   String nameRu,
                   String nameEn,
                   String head,
                   String code,
                   String area,
                   String address,
                   String bik,
                   String acc,
                   String phone,
                   String path,
                   String newPath,
                   int state,
                   String tger,
                   LocalDateTime createdDate,
                   String createdBy,
                   int isActive,
                   List<Organization> children
    ) {
        this.id = id;
        this.parentId = parentId;
        this.nameKz = nameKz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.head = head;
        this.code = code;
        this.area = area;
        this.address = address;
        this.bik = bik;
        this.acc = acc;
        this.phone = phone;
        this.path = path;
        this.newPath = newPath;
        this.state = state;
        this.tger = tger;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.isActive = isActive;
        this.children = children;
    }
}
