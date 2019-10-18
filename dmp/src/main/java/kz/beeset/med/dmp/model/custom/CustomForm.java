package kz.beeset.med.dmp.model.custom;

import kz.beeset.med.constructor.model.guide.Form;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CustomForm extends Form {

    private boolean hasWarehouse;
    private Map<String, Boolean> fulfilledMap;

    public CustomForm(Form form, boolean hasWarehouse) {
        this.setId(form.getId());
        this.setNameKz(form.getNameKz());
        this.setNameRu(form.getNameRu());
        this.setNameEn(form.getNameEn());
        this.setDescriptionKz(form.getDescriptionKz());
        this.setDescriptionRu(form.getDescriptionRu());
        this.setDescriptionEn(form.getDescriptionEn());
        this.setTypeForm(form.getTypeForm());
        this.setGlobal(form.isGlobal());
        this.setOrganizationId(form.getOrganizationId());
        this.setCode(form.getCode());
        this.hasWarehouse = hasWarehouse;
    }

    public CustomForm(Form form, boolean hasWarehouse, Map<String, Boolean> fulfilledMap) {
        this.setId(form.getId());
        this.setNameKz(form.getNameKz());
        this.setNameRu(form.getNameRu());
        this.setNameEn(form.getNameEn());
        this.setDescriptionKz(form.getDescriptionKz());
        this.setDescriptionRu(form.getDescriptionRu());
        this.setDescriptionEn(form.getDescriptionEn());
        this.setTypeForm(form.getTypeForm());
        this.setGlobal(form.isGlobal());
        this.setOrganizationId(form.getOrganizationId());
        this.setCode(form.getCode());
        this.hasWarehouse = hasWarehouse;
        this.fulfilledMap = fulfilledMap;
    }
}
