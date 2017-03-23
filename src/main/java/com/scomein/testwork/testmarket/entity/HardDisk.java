package com.scomein.testwork.testmarket.entity;

import com.scomein.testwork.testmarket.csv.ProductType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
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

    public HardDisk() {
        type = ProductType.hdd;
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

        for (FIELD_NAMES name : FIELD_NAMES.values()) {
            if (name.name().equals(fieldName)) {
                FIELD_NAMES.valueOf(fieldName).setValue(this, fieldValue);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> parseToRow() {
        List<String> data = super.parseToRow();
        if (size != null) {
            data.add(FIELD_NAMES.size + ":" + size);
        }
        return data;
    }


    @Override
    public CriteriaQuery<HardDisk> getQuery(CriteriaBuilder builder) {
        CriteriaQuery<HardDisk> criteriaQuery = builder.createQuery(HardDisk.class);
        Root<HardDisk> root = criteriaQuery.from(HardDisk.class);
        criteriaQuery.select(root);
        criteriaQuery.where(Product.build(this, builder, root),
                builder.equal(root.get("size"), size));

        return criteriaQuery;
    }
}
