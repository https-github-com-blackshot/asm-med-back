package kz.beeset.med.dmp.model.dashboard;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DashboardWidget {

    private String title;
    private String detail;
    private String dataLabel;
    private Integer dataCount;
    private String backDataLabel;
    private Integer backDataCount;
    private String dataExtraLabel;
    private String link;

}
