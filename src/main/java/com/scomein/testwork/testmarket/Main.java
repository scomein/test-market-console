package com.scomein.testwork.testmarket;

import com.scomein.testwork.testmarket.csv.CsvService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Created by scome on 16.03.17.
 */
@Component
@Configuration
@EntityScan(basePackages = {"com.scomein.testwork.entity"})
@ComponentScan(basePackages = {"com.scomein.testwork.testmarket"})
public class Main {

    public static final String EXPORT = "export";

    public static final String IMPORT = "import";

    public static final String END = "exit";

    @Autowired
    private CsvService csvService;

    @Bean
    SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext();
        Main main = (Main) ctx.getBean("Main");
        main.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("enter command:\n");
            String command = scanner.nextLine();
            int index = command.indexOf(" ");
            if (index < 0) {
                System.out.println("unknown command.");
                continue;
            }

            switch (command.substring(0, index)) {
                case EXPORT:
                    csvService.exportFile(command.substring(index + 1));
                    break;
                case IMPORT:
                    csvService.importFile(command.substring(index + 1));
                    break;
                case END:
                    return;
                default:
                    System.out.println("unknown command.");
            }
        }
    }
}
