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
@Table(name = "monitor")
public class Monitor extends Product {

    @Column
    private Float diagonal;

    enum FIELD_NAMES {
        diagonal {
            @Override
            void setValue(Monitor product, String value) {
                product.setDiagonal(Float.valueOf(value));
            }
        };

        abstract void setValue(Monitor product, String value);
    }

    public float getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(float diagonal) {
        this.diagonal = diagonal;
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
        if (diagonal != null) {
            data.add(FIELD_NAMES.diagonal + ":" + diagonal);
        }
        return data;
    }
}
