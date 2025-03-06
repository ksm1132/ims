package com.ims.inventorymgmtsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InventorymgmtsysApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorymgmtsysApplication.class, args);
	}

}
