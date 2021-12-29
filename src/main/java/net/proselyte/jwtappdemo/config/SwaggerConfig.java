package net.proselyte.jwtappdemo.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //.apis(RequestHandlerSelectors.basePackage("net.proselyte.jwtappdemo"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    public ApiInfo apiInfo(){
        return new ApiInfo(
                "Marvel documentation API",
                "Marvel documentation API",
                "1.0",
                "",
                new Contact("Danil Cherepanov","https://github.com/dcherepanov2/","danilcherep7@gmail.com"),
                "Api license",
                "https://www.license.edu.org",//TODO: затычка, заменить при выгрузке в production
                new ArrayList<>()
        );
    }
}
