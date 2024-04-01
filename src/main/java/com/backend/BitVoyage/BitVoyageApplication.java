package com.backend.BitVoyage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//scanBasePackages에서 경로 지정
@SpringBootApplication(scanBasePackages = {"net.codejava.BitVoyageApp"})
public class BitVoyageApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitVoyageApplication.class, args);
	}

}
