package com.moshood.mwta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoshoodWalletTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoshoodWalletTransactionApplication.class, args);
	}

}
