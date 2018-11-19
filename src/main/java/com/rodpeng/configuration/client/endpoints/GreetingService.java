package com.rodpeng.configuration.client.endpoints;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "GreetingService", url = "http://localhost:8761")
public interface GreetingService {
	
	@NewSpan
	@RequestMapping(method = RequestMethod.GET, value = "/greeting/{name}")
    String getMessage(@PathVariable("name") String name);
}
