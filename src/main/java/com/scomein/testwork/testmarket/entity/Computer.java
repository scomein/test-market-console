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

    public Computer() {
        type = ProductType.computer;
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
        if (formFactor != null) {
            data.add(FIELD_NAMES.formfactor.name() + ":" + formFactor.name());
        }
        return data;
    }

    @Override
    public CriteriaQuery<Computer> getQuery(CriteriaBuilder builder) {
        CriteriaQuery<Computer> criteriaQuery = builder.createQuery(Computer.class);
        Root<Computer> root = criteriaQuery.from(Computer.class);
        criteriaQuery.select(root);
        criteriaQuery.where(Product.build(this, builder, root),
                builder.equal(root.get("formFactor"), formFactor));

        return criteriaQuery;
    }
}
