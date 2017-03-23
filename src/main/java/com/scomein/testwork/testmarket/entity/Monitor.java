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

    public Monitor() {
        type = ProductType.monitor;
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
        if (diagonal != null) {
            data.add(FIELD_NAMES.diagonal + ":" + diagonal);
        }
        return data;
    }


    @Override
    public CriteriaQuery<Monitor> getQuery(CriteriaBuilder builder) {
        CriteriaQuery<Monitor> criteriaQuery = builder.createQuery(Monitor.class);
        Root<Monitor> root = criteriaQuery.from(Monitor.class);
        criteriaQuery.select(root);
        criteriaQuery.where(Product.build(this, builder, root),
                builder.equal(root.get("diagonal"), diagonal));

        return criteriaQuery;
    }
}
