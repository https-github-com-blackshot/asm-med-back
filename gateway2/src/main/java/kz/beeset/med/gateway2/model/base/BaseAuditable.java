package kz.beeset.med.gateway2.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BaseAuditable {
    @CreatedBy
    @JsonIgnore
    protected String createdBy;

    @JsonIgnore
    @CreatedDate
    protected LocalDateTime createdDate;

    @JsonIgnore
    @LastModifiedBy
    protected String lastModifiedBy;

    @JsonIgnore
    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;

}
