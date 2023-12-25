package com.example.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API",
                version = "1.0.0",
                description = "API swagger definition",
                termsOfService = "Terms of service",
                contact = @Contact(name = "Ilija Stojmirovic", email = "istojmirovic4521rn@raf.rs")
        )
)
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("userserivsapp")
                .packagesToScan("com.example.controller")
                .build();
    }
}
