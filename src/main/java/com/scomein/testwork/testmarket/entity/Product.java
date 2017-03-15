package com.scomein.testwork.testmarket.entity;

/**
 * Created by scome on 15.03.17.
 */

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {

    @Column
    private String serialNumber;

    @Column
    private String producer;

    @Column
    private double price;

    @Column
    private int count;

    public enum FIELD_NAMES {
        serialnumber {
            @Override
            void setValue(Product product, String value) {
                product.setSerialNumber(value);
            }
        }, producer {
            @Override
            void setValue(Product product, String value) {
                product.setProducer(value);
            }
        }, price {
            @Override
            void setValue(Product product, String value) {
                product.setPrice(Double.valueOf(value));
            }
        };

        abstract void setValue(Product product, String value);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean fillField(String fieldName, String fieldValue) {
        if (FIELD_NAMES.valueOf(fieldName) == null) {
            return false;
        }

        FIELD_NAMES.valueOf(fieldName).setValue(this, fieldValue);
        return true;
    }
}
