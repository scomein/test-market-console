package com.scomein.testwork.testmarket.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by scome on 15.03.17.
 */

@Entity
@Table(name = "hard_disk")
public class HardDisk extends Product {

    @Column
    private Double size;

    enum FIELD_NAMES {
        size {
            @Override
            void setValue(HardDisk product, String value) {
                product.setSize(Double.valueOf(value));
            }
        };

        abstract void setValue(HardDisk product, String value);
    }


    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
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
        if (size != null) {
            data.add(FIELD_NAMES.size + ":" + size);
        }
        return data;
    }
}
