package kz.beeset.med.admin.model.catalog;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "city")
@Getter
@Setter
@EqualsAndHashCode
public class City {
    @Id
    private String id;
    private String name;
}
