package org.test.bookpub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookpubApplication {

	@Bean
	public StartupRunner schedulerRunner() {
		return new StartupRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(BookpubApplication.class, args);
	}
}
