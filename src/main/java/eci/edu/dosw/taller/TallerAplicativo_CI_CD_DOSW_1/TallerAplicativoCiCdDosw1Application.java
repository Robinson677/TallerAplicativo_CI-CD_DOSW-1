package eci.edu.dosw.taller.TallerAplicativo_CI_CD_DOSW_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"eci.edu.dosw.taller", "eci.edu.dosw.taller.TallerAplicativo_CI_CD_DOSW_1"})
public class TallerAplicativoCiCdDosw1Application {

    public static void main(String[] args) {
        SpringApplication.run(TallerAplicativoCiCdDosw1Application.class, args);
    }
}
