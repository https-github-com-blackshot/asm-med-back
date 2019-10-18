package kz.beeset.med.dmp.model.dmpv2;

import lombok.Getter;
import lombok.Setter;

import javax.tools.Diagnostic;
import java.util.List;

@Getter
@Setter
public class DiseaseData {
    private String subDannie;
    private String anamBol;
    private String anamZhiz;
    private String objDannie;
    private List<String> labAnalys;
    private String labAnalysText;
    private List<String> diagMethods;
    private String diagMethodsText;
    private String diagZakl;
    private List<String> lekNazn;
    private String lekNaznText;
    private List<String> procAndInter;
    private String procAndInterText;
    private String vrachRec;
    private String sovNomadiet;
    private String infoMat;
    private String note;
}
