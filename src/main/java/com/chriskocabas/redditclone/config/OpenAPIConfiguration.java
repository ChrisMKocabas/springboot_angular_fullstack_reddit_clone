package com.chriskocabas.redditclone.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@OpenAPIDefinition
@Configuration
public class OpenAPIConfiguration {


    @Bean
    public OpenAPI redditAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reddit Spring Boot API")
                        .description("API for Reddit Application")
                        .version("v1.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")))
                                .externalDocs(new ExternalDocumentation()
                                          .description("Chris Kocabas, chriskocabas@outlook.com")
                                          .url("https://www.chriskocabas.com"));


    }
}