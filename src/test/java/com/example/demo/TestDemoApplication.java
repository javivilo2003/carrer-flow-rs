package com.example.demo;

import org.springframework.boot.SpringApplication;

import app.careerflow.rs.DemoApplication;

public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
