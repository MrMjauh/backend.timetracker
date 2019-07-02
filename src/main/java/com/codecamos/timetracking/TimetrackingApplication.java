package com.codecamos.timetracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class TimetrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetrackingApplication.class, args);
	}
}
