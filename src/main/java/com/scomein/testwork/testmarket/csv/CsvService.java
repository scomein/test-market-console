package com.scomein.testwork.testmarket.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.scomein.testwork.testmarket.Dao.ProductDao;
import com.scomein.testwork.testmarket.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvService.class);

    @Autowired
    private ProductDao dbService;


    public static String[] toArray(List<String> list) {
        String[] data = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }

    public void importFile(String fileName) {
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName), SEPARATOR);
            String[] row;
            while ((row = reader.readNext()) != null) {
                createAndSave(getProductType(row[COLUMN_PRODUCT_TYPE]), row);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("File " + fileName + " not found!");
        } catch (IOException e) {
            LOGGER.error("Some exception occured while parsing file " + fileName +
                    ". Exception: " + e.getMessage());
        }
    }

    protected ProductType getProductType(String name) {
        for (ProductType type : ProductType.values()) {
            if (type.name().equals(name.toLowerCase())) {
                return type;
            }
        }
        return null;
    }

    protected void createAndSave(ProductType type, String[] data) {
        if (type == null) {
            LOGGER.warn("can't create Product with type " + data[0]);
            return;
        }
        Product product = create(type);
        fill(product, data);
        save(product);
    }

    protected Product create(ProductType type) {
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
            String value = data[i];
            int index = value.indexOf(":");
            if (index < 0 || index == value.length() - 1) {
                LOGGER.warn("Problem in file: parameter "
                        + value
                        + "hasn't symbol ':' and will not be parsed");
                continue;
            }

            String fieldName = value.substring(0, index).toLowerCase();
            String fieldValue = value.substring(index + 1);

            if (!product.fillField(fieldName, fieldValue)) {
                LOGGER.warn("Problem in file: can't fill parameter "
                        + fieldName
                        + "(value: " + fieldValue + ") for object " + product.getClass().getSimpleName());

            }
        }

        return product;
    }

    protected void save(Product product) {
        dbService.save(product);
    }

    public void exportFile(String fileName) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName), SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER)) {
            List<Product> products = getPart(PART_SIZE);
                for (Product product : products) {
                    writer.writeNext(toArray(product.parseToRow()));
                }
        } catch (IOException e) {
            LOGGER.error("Some exception occured while import data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected List<Product> getPart(int partSize) {
        return dbService.findAll(partSize);
    }
}
