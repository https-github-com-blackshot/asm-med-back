package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.beeset.med.dmp.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_notification")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserNotification extends BaseAuditable {

    @Id
    private String id;
    private String userId;
    private LocalDateTime regDate;
    private String title;
    private String text;
    /**
     * Статус
     *  1 Активный
     *  5 Удалено
     */
    @JsonIgnore
    private int state;

}
