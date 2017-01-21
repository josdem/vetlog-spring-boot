package com.jos.dem.vetlog

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.web.SpringBootServletInitializer

@SpringBootApplication
class VetlogApplication extends SpringBootServletInitializer{

	static void main(String[] args) {
		SpringApplication.run VetlogApplication, args
	}
}
