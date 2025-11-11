package com.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.backend.service.Server.server;


@SpringBootApplication
public class BackendApplication {



	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		server();

	}




}
