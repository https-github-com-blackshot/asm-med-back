package kz.beeset.med.gateway2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ApplicationSwaggerConfig {
    @Bean
    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("kz.beeset.med.gateway2"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Cloudoc backend Api Documentation")
                .description("This is REST API documentation of the Spring Cloudoc backend. If authentication is enabled, when calling the APIs use admin/admin")
                .version("1.0").termsOfServiceUrl("Cloudoc backend terms of service")
                .license("")
                .licenseUrl("")
                .build();
//        return new ApiInfo(
//                "Cloudoc backend Api Documentation",
//                "This is REST API documentation of the Spring Cloudoc backend. If authentication is enabled, when calling the APIs use admin/admin",
//                "1.0",
//                "Cloudoc backend terms of service",
//                "Admin Admin",
//                "",
//                "");
    }
}
