package com.codecamos.timetracking;

import com.codecamos.timetracking.config.BaseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class TimetrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetrackingApplication.class, args);
	}

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello World!";
	}


	@GetMapping("/runtime")
	public void runtime() {
		throw new BaseException(0, "No bueno");
	}
}
