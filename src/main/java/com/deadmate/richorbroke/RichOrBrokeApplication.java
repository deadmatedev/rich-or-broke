package com.deadmate.richorbroke;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Generated
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class RichOrBrokeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RichOrBrokeApplication.class);
	}
}
