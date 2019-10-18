package kz.beeset.med.constructor.constant;

import java.util.ArrayList;
import java.util.List;

public class FormTypeConstant {
    public final static FormTypeObject SCRINING= new FormTypeObject("Является скринингом","screening", setSubTypes("scrining"));
    public final static FormTypeObject GRID= new FormTypeObject("Является гридом", "grid", setSubTypes("grid"));
    public final static FormTypeObject FORM= new FormTypeObject("Является формой", "form", setSubTypes("form"));
    public final static FormTypeObject FIELD_GROUP= new FormTypeObject("Field group", "field_group", setSubTypes("field_group"));
    public final static FormTypeObject TABLE = new FormTypeObject("TABLE", "table", setSubTypes("table"));

    private static List<FormTypeObject> setSubTypes(String formType) {

        FormTypeObject NYA = new FormTypeObject("Nya", "nya");
        FormTypeObject GENERAL = new FormTypeObject("General", "general");
        FormTypeObject STATE = new FormTypeObject("State", "state");

        List<FormTypeObject> subTypes = new ArrayList<>();

        if (formType.equals("form") || formType.equals("grid") ){
            subTypes.add(NYA);
            subTypes.add(GENERAL);
            subTypes.add(STATE);
        }

        return subTypes;
    }


}

