package kz.beeset.med.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import kz.beeset.med.admin.repository.ResourceUtilRepositoryImpl;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories(repositoryBaseClass = ResourceUtilRepositoryImpl.class)
@EnableMongoAuditing
@ComponentScan(basePackages = {"kz.beeset.med"})
public class AdminApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AdminApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
