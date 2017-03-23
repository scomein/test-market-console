package com.scomein.testwork.testmarket;

import com.scomein.testwork.testmarket.csv.CsvService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Created by scome on 16.03.17.
 */
@Component
@Configuration
@ComponentScan(basePackages = {"com.scomein.testwork.testmarket"})
@SpringBootApplication
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

    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        Main main = context.getBean(Main.class);
        main.context = context;
        main.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("enter command:\n");
            String command = scanner.nextLine();

            if (command.equals(END)) {
                SpringApplication.exit(context);
                return;
            }

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
                default:
                    System.out.println("unknown command.");
            }
        }
    }
}
