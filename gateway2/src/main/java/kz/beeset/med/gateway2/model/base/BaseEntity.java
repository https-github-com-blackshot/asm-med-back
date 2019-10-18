package kz.beeset.med.gateway2.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BaseEntity {
   @Id
   private String id;

   @JsonIgnore
   private int state;
   @JsonIgnore
   @LastModifiedDate
   private LocalDateTime lastModifiedDate;
   @JsonIgnore
   @LastModifiedBy
   private String lastModifiedBy;
   @JsonIgnore
   @CreatedDate
   private LocalDateTime createdDate;
   @JsonIgnore
   @CreatedBy
   private String createdBy;
}
