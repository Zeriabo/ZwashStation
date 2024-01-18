package com.zwash.station;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.zwash.common.repository")
public class ZwashStationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashStationApplication.class, args);
	}

}
