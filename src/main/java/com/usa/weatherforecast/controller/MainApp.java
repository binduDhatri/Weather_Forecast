package com.usa.weatherforecast.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = WeatherController.class)
public class MainApp {

	public static void main( String[] args ) {
	      SpringApplication.run( MainApp.class, args );
	   }
}
