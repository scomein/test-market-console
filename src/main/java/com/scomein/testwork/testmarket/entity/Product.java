package com.scomein.testwork.testmarket.entity;

/**
 * Created by scome on 15.03.17.
 */

import com.scomein.testwork.testmarket.csv.ProductType;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "product")
@NamedQueries(
        @NamedQuery(name = "getAll",
                query = "from Product")
)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String serialNumber;

    @Column
    private String producer;

    @Column
    private Double price;

    @Column
    private Integer count;

    protected ProductType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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

    public Double getPrice() {
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
        for (FIELD_NAMES name : FIELD_NAMES.values()) {
            if (name.name().equals(fieldName)) {
                FIELD_NAMES.valueOf(fieldName).setValue(this, fieldValue);
                return true;
            }
        }
        return false;
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

    public abstract <T extends Product> CriteriaQuery<T> getQuery(CriteriaBuilder builder);

    public static <T extends Product> Predicate build(Product product, CriteriaBuilder builder, Root<T> root) {
        List<Predicate> predicates = Arrays.asList(
                andIfNotNull(builder, root.get("serialNumber"), product.getSerialNumber()),
                andIfNotNull(builder, root.get("producer"), product.getProducer()),
                andIfNotNull(builder, root.get("price"), product.getPrice()));
        Predicate predicate = builder.and();
        for (Predicate pr : predicates) {
            if (pr != null) {
                predicate = builder.and(predicate, pr);
            }
        }
        return predicate;
    }

    public static Predicate andIfNotNull(CriteriaBuilder builder, Expression expression, Object val) {
        if (val == null) {
            return null;
        }
        return builder.equal(expression, val);
    }


}
