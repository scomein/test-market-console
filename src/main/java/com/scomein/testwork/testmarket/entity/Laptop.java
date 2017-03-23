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
@Table(name = "laptop")
public class Laptop extends Product {

    @Column
    private Float diagonal;

    enum FIELD_NAMES {
        diagonal {
            @Override
            void setValue(Laptop product, String value) {
                product.setDiagonal(Float.valueOf(value));
            }
        };

        abstract void setValue(Laptop product, String value);
    }

    public Laptop() {
        type = ProductType.laptop;
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
    public CriteriaQuery<Laptop> getQuery(CriteriaBuilder builder) {
        CriteriaQuery<Laptop> criteriaQuery = builder.createQuery(Laptop.class);
        Root<Laptop> root = criteriaQuery.from(Laptop.class);
        criteriaQuery.select(root);
        criteriaQuery.where(Product.build(this, builder, root),
                builder.equal(root.get("diagonal"), diagonal));

        return criteriaQuery;
    }
}
