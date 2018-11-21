package com.rodpeng.configuration.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rodpeng.configuration.client.endpoints.GreetingService;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableZuulProxy
@EnableCircuitBreaker
@EnableHystrix
public class ConfigClientApplication {
	public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }
}

@RefreshScope
@RestController
class MessageRestController {
	public static Logger LOGGER = LoggerFactory.getLogger(MessageRestController.class);
	
	@Autowired
	private GreetingService greetingService;

    @Value("${message:Hello default}")
    private String message;
    
    @Value("${password: default}")
    private String password;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;		
    }
    
    @RequestMapping("/password")
    String getPassword() {
        return this.password;
    }
    
    public String fallback(String name) {
    	return "ohai!";
    }
    
    @NewSpan
    @HystrixCommand	(fallbackMethod = "fallback")
    @RequestMapping(method = RequestMethod.GET, value = "/greeting/{name}")
    String greeting(@PathVariable("name") String name) {
    	String result = "Haha " +  greetingService.getMessage(name);
    	LOGGER.info("Greeting from {}: {}", name, result);
    	
    	return result;
    }
}