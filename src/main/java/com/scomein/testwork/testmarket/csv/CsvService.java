package com.scomein.testwork.testmarket.csv;

import au.com.bytecode.opencsv.CSVReader;
import com.scomein.testwork.testmarket.entity.Computer;
import com.scomein.testwork.testmarket.entity.Product;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * Created by scome on 15.03.17.
 */

@Service
public class CsvService {

    public static int COLUMN_PRODUCT_TYPE = 0;
    public static int COLUMN_COUNT = 1;

    public void importFile(String fileName) {
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName));
            String[] row;
            while ((row = reader.readNext()) != null) {
                createAndSave(ProductType.valueOf(row[COLUMN_PRODUCT_TYPE].toLowerCase()), row);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createAndSave(ProductType type, String[] data) {
        switch (type) {
            case computer:
                break;
            case laptop:
                break;
            case monitor:
                break;
            case hdd:
                break;
        }
    }

    protected void createComputer(String[] data) {
        Computer computer = new Computer();
        computer.setCount(Integer.valueOf(data[COLUMN_COUNT]));

        for (int counter = COLUMN_COUNT + 1; counter < data.length; counter++) {
            String fieldName = data[counter++];
            if (counter == data.length) {
                // log this
                return;
            }

            String fieldValue = data[counter];

            if (!computer.fillField(fieldName, fieldValue)) {
                // log this
                continue;
            }

        }
    }

    public void exportFile(String fileName) {

    }
}
