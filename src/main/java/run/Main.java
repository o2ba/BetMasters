package run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = {"controller", "service", "service", "service.general", "dto.request"})
public class Main {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Main.class, args);
        } catch (Exception e) {
            System.out.println("Error starting application: " + e.getMessage());
        }
    }
}