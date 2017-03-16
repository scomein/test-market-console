package com.scomein.testwork.testmarket.entity;

/**
 * Created by scome on 15.03.17.
 */

import com.scomein.testwork.testmarket.csv.ProductType;

import javax.persistence.*;
import java.util.Arrays;
import java.util.LinkedList;
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
    private Double price;

    @Column
    private Integer count;

    protected ProductType type;


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

    public List<String> parseToRow() {
        List<String> data = new LinkedList<>();
        data.add(type.name());
        data.add(count + "");
        addIfNeed(data, FIELD_NAMES.producer, producer);
        addIfNeed(data, FIELD_NAMES.serialnumber, serialNumber);
        addIfNeed(data, FIELD_NAMES.price, price);
        return data;
    }

    protected void addIfNeed(List<String> data, FIELD_NAMES field, Object value) {
        if (value != null) {
            data.add(field.name() + ":" + value);
        }
    }
}
