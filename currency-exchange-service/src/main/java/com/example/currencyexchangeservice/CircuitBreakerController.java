package com.example.currencyexchangeservice;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;



@RestController
public class CircuitBreakerController {
	
	private Logger logger=
			LoggerFactory.getLogger(CircuitBreakerController.class);
	@GetMapping("/sample-api")
	//@Retry(name="sample-apiX", fallbackMethod="hardcodedResponse")
	@CircuitBreaker(name="default", fallbackMethod="hardcodedResponse")
	@RateLimiter(name="default")
	
	public String sampleApi() {
		logger.info("Sample api call received");
		ResponseEntity<String> forEntity=new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url",
				String.class);
		return forEntity.getBody();
	}
	
	@GetMapping("/sample-api2")
	@RateLimiter(name="defaultY")
	//En 10 desgundos quiero permitir solo 10,000 llamadas a esta API
	public String sampleApi2() {
		logger.info("Sample api call received");
	
		return "sample-api2";
	}
	
	@GetMapping("/sample-api3")
	@Bulkhead(name="defaultZ")
	//Solo tienes permitido 10 llamadas concurrentes (mismo tiempo) a esta API
	public String sampleApi3() {
		logger.info("Sample api call received");
		return "sample-api3";
	}
	
	
	
	
	
	
	public String hardcodedResponse(Exception ex) {
		return "fallback-response";
	}
}
