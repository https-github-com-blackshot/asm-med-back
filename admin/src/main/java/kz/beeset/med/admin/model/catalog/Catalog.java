package kz.beeset.med.admin.model.catalog;

import kz.beeset.med.admin.model.BaseAuditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "catalog")
@Getter
@Setter
@EqualsAndHashCode
public class Catalog {
    @Id
    private String id;
    private String name;
    private String urlCode;
    private List<CVariable> variableList;
}
