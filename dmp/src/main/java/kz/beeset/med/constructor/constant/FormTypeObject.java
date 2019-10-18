package kz.beeset.med.constructor.constant;

import lombok.Getter;

import java.util.List;

@Getter
public class FormTypeObject {
    String name;
    String code;
    List<FormTypeObject> subTypes;

    public FormTypeObject(String name, String code, List<FormTypeObject> subTypes) {
        this.name = name;
        this.code = code;
        this.subTypes = subTypes;
    }

    public FormTypeObject(String name, String code) {
        this.name = name;
        this.code = code;
    }

}