package kz.beeset.med.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "role")
@Getter
@Setter
@EqualsAndHashCode
public class Role {
    @Id
    private String id;
    private String name;
    private String code;
    private String description;
    private List<String> rights;
    /**
     * Статус
     * 1 Активный
     * 5 Удалено
     */
    @JsonIgnore
    private int state;

    @JsonIgnore
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonIgnore
    @CreatedBy
    private String createdBy;
}
