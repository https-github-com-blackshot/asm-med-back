package kz.beeset.med.admin.model.common;

import lombok.Data;

import java.util.List;

@Data
public class Pages {
    private List<?> content;
    private Long totalRecords;
    private int totalPage;
}
