package com.example.BankingApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// OpenAPI Documentation
@OpenAPIDefinition(
		info = @Info(
				title = "Banking Application",
				description = "Backend RestAPI for Banking Application",
				version = "v1.0",
				contact = @Contact(
						name = "Sandip Kumar Shah",
						email = "rdjtonystark97875@gmail.com",
						url = "https://github.com/sandip01234/BankingApplication"
				),
				license = @License(
						name = "Springboot Learning",
						url = "https://github.com/sandip01234/BankingApplication"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Springboot Learning Resources",
				url = "https://github.com/sandip01234/BankingApplication"
		)
)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
