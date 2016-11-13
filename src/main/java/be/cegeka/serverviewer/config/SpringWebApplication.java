package be.cegeka.serverviewer.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("be.cegeka.serverviewer")
@EnableJpaRepositories("be.cegeka.serverviewer")
@EntityScan("be.cegeka.serverviewer")
public class SpringWebApplication {

    public static void main(String[] args){
        SpringApplication.run(SpringWebApplication.class, args);
    }

}
