package com.quantumzone.QZ_Workhub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration

public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("QZ-WorkHub API")
                        .version("v1.0")
                        .description("API documentation for QZ-WorkHub API application"));
    }
    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone(); //Usa el reloj del sistema
    }

}
