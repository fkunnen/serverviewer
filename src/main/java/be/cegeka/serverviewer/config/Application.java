package be.cegeka.serverviewer.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("be.cegeka.serverviewer.server")
@EnableJpaRepositories("be.cegeka.serverviewer.server")
@EntityScan("be.cegeka.serverviewer.server")
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

}
