package com.gmail.ramawathar.priyash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mangofactory.swagger.plugin.EnableSwagger;

@SpringBootApplication
@EnableSwagger 
public class MyBudgetApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBudgetApplication.class, args);
	}
}
