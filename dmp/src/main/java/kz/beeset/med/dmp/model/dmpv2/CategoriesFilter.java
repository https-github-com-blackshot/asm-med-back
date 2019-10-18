package kz.beeset.med.dmp.model.dmpv2;

import kz.beeset.med.dmp.utils.error.InternalException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class CategoriesFilter {

    private String name;
    private String code;

    public static List<CategoriesFilter> getFilter() throws InternalException {
        List<CategoriesFilter> filter = new ArrayList<>();

        CategoriesFilter filter1 = new CategoriesFilter();
        filter1.code = "diseases";
        filter1.name = "Заболевания";

        CategoriesFilter filter2 = new CategoriesFilter();
        filter2.code = "laboratory";
        filter2.name = "Лаборатория";

        CategoriesFilter filter3 = new CategoriesFilter();
        filter3.code = "diagnostics";
        filter3.name = "Диагностика";

        CategoriesFilter filter4 = new CategoriesFilter();
        filter4.code = "medicines";
        filter4.name = "Лекарства";

        CategoriesFilter filter5 = new CategoriesFilter();
        filter5.code = "protocols";
        filter5.name = "Протокола";

        CategoriesFilter filter6 = new CategoriesFilter();
        filter6.code = "proceduresAndInterventions";
        filter6.name = "Процедуры и вмешательства";

        filter.add(filter1);
        filter.add(filter2);
        filter.add(filter3);
        filter.add(filter4);
        filter.add(filter5);
        filter.add(filter6);

        return filter;
    }

}
