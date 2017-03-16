package com.scomein.testwork.testmarket.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

/**
 * Created by scome on 15.03.17.
 */

@Entity
@Table(name = "computer")
public class Computer extends Product {

    public enum FormFactor {
        desktop,
        monoblock,
        nettop
    }

    enum FIELD_NAMES {
        formfactor {
            @Override
            void setValue(Computer product, String value) {
                FormFactor formFactor = FormFactor.valueOf(value.toLowerCase());
                if (formFactor == null) {
                    return;
                }
                product.setFormFactor(formFactor);
            }
        };

        abstract void setValue(Computer product, String value);
    }


    @Column
    private FormFactor formFactor;


    public FormFactor getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(FormFactor formFactor) {
        this.formFactor = formFactor;
    }

    @Override
    public boolean fillField(String fieldName, String fieldValue) {
        if (super.fillField(fieldName, fieldValue)) {
            return true;
        }

        FIELD_NAMES field = FIELD_NAMES.valueOf(fieldName);
        if (field == null) {
            return false;
        }

        field.setValue(this, fieldValue);
        return true;
    }

    @Override
    public List<String> parseToRow() {
        List<String> data = super.parseToRow();
        data.add(FIELD_NAMES.formfactor.name() + ":" + formFactor.name());
        return data;
    }
}
