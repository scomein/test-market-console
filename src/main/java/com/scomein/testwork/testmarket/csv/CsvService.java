package com.scomein.testwork.testmarket.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.scomein.testwork.testmarket.entity.*;
import com.sun.istack.internal.NotNull;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scome on 15.03.17.
 */

@Service
public class CsvService {

    public static final int COLUMN_PRODUCT_TYPE = 0;
    public static final int COLUMN_COUNT = 1;

    public static final char SEPARATOR = ';';
    public static final int PART_SIZE = 50;

    public void importFile(String fileName) {
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName));
            String[] row;
            while ((row = reader.readNext()) != null) {
                createAndSave(ProductType.valueOf(row[COLUMN_PRODUCT_TYPE].toLowerCase()), row);
            }
        } catch (FileNotFoundException e) {
            //log this
            e.printStackTrace();
        } catch (IOException e) {
            //log this
            e.printStackTrace();
        }
    }

    protected void createAndSave(ProductType type, String[] data) {
        Product product = create(type);
        fill(product, data);
        save(product);
    }

    protected Product create(@NotNull ProductType type) {
        switch (type) {
            case computer:
                return new Computer();
            case laptop:
                return new Laptop();
            case monitor:
                return new Monitor();
            default:
                return new HardDisk();
        }
    }

    protected Product fill(Product product, String[] data) {
        product.setCount(Integer.valueOf(data[COLUMN_COUNT]));
        for (int i = COLUMN_COUNT + 1; i < data.length; i++) {
            int index = data[i].indexOf(":");
            if (index < 0 || index == data[i].length() - 1) {
                //log this
                continue;
            }

            if (!product.fillField(data[i].substring(0, index), data[i].substring(index + 1))) {
                //log this
            }
        }

        return product;
    }

    protected void save(Product product) {
        // TODO: 16.03.17 realize saving after creating DBService
    }

    public void exportFile(String fileName) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName), SEPARATOR);
            List<Product> products = getPart(PART_SIZE);
            while (products.size() > 0) {
                for (Product product : products) {
                    writer.writeNext(product.parseToRow().toArray(new String[0]));
                }
            }

        } catch (IOException e) {
            //log this
            e.printStackTrace();
        }
    }

    protected List<Product> getPart(int partSize) {
        // TODO: 16.03.17 realize getting objects from db after creating DBService

        return new ArrayList<>();
    }
}
