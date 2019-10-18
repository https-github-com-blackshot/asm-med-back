package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "profile")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Profile extends BaseAuditable {

    /**
     * Идентификатор
     */
    @Id
    private String id;

    /**
     * Идентификатор пользователя
     */
    private String userId;

    /**
     * Краткая информация о пациенте
     */
    private ProfileInfo info;

    /**
     * Доктора(userId) у которых есть доступ к данным пациента
     */
    private List<String> doctors;

    /**
     * Статус здоровья
     */
    private Integer healthStatus;

    /**
     * Статус
     * 1 Активный
     * 5 Удалено
     */
    @JsonIgnore
    private int state;
}
