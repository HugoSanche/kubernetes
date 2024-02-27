package com.example.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {
	
	private Logger logger=LoggerFactory.getLogger(CurrencyExchangeController.class);
	
	@Autowired
	private CurrencyExchangeRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(
    												@PathVariable String from,
    												@PathVariable String to) 
    {
    	
    	
    	//INFO [currency-exchange,f6efd0b3a68fdec7240ae8bbb5775dcf,5b1931be7d31ca47]  66643 --- [currency-exchange] [nio-8000-exec-1] [f6efd0b3a68fdec7240ae8bbb5775dcf-5b1931be7d31ca47] c.e.c.CurrencyExchangeController         : retrieveExchangeValue called with USD to INR
    	//id f6efd0b3a68fdec7240ae8bbb5775dcf   == from
    	//id 5b1931be7d31ca47                   == to
    	logger.info("retrieveExchangeValue called with {} to {}", from, to); //obtiene los id de from y to 
    	CurrencyExchange currencyExchange=repository.findByFromAndTo(from,to);
       if(currencyExchange==null) {
    	   throw new RuntimeException
    	   ("Unable to find data for "+from+" to "+to);
       }
        
        String port = environment.getProperty("local.server.port");
        
      //CHANGE-KUBERNETES
      		String host = environment.getProperty("HOSTNAME");
      		String version = "v11";
        
        
      		currencyExchange.setEnvironment(port + " " + version + " " + host);
        
        return currencyExchange;

    }

}