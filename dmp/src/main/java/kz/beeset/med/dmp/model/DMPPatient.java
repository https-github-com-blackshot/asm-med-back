package kz.beeset.med.dmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "dmp_patient")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DMPPatient extends BaseAuditable{
    @Id
    private String id;
    @Indexed
    private String dmpId;
    private String userId;
    private String codeNumber;
    private List<String> dmpDoctorIds;

    /**
     * 1 - Показания отклонены от нормы
     * 2 - Показания в пределах границ
     * 3 - Показание в норме
     * 4 - Устройство не настроено
     * 5 - Нет данныз с браслета
     * 6 - Устройство не зарегистрировано
     */
    private int healthStatus;

    @JsonIgnore
    private int state;
}
