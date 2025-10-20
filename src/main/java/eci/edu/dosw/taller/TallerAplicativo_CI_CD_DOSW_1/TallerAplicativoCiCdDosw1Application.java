package eci.edu.dosw.taller.TallerAplicativo_CI_CD_DOSW_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Clase principal
 */
@SpringBootApplication
@ComponentScan(basePackages = "eci.edu.dosw.taller")
@EnableMongoRepositories(basePackages = "eci.edu.dosw.taller.repositories")
public class TallerAplicativoCiCdDosw1Application {
    public static void main(String[] args) {
        SpringApplication.run(TallerAplicativoCiCdDosw1Application.class, args);
    }
}
