package com.iwomi.External_api_cccNewUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ExternalApiCccNewUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExternalApiCccNewUpApplication.class, args);
	}

}
