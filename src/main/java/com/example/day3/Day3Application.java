// Day3Application.java
package com.example.day3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Day3Application {

	public static void main(String[] args) {
		SpringApplication.run(Day3Application.class, args);
	}
}
