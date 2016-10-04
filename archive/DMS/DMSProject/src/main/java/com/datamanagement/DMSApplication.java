package com.datamanagement;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
public class DMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(DMSApplication.class, args);
	}
	
	/**
	 * method to set the Locale value which will be used across the application
	 * 
	 * @return
	 */
	@Bean
	public SessionLocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}
}
