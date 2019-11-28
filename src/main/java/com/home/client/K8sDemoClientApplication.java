package com.home.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@SpringBootApplication
@RestController
public class K8sDemoClientApplication {

    public static final String serverUrl = "http://localhost:9090";
    @Autowired
    RestTemplate restTemplate;

    public  static void main(String[] args) {
        SpringApplication.run(K8sDemoClientApplication.class, args);
    }


    @GetMapping("/")
    public static String start() {
        return "What do you want to do?";
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @GetMapping("/codeToState")
    public String CodeToState(@RequestParam("code") String code) {
        Map<String, String> response = restTemplate.getForObject(serverUrl + "/readDataForCode", Map.class);
        return response.getOrDefault(code, "Found no state with code " + code );
    }

    @GetMapping("/stateToCode")
    public Object StateToCode(@RequestParam("state") String state) {
       State[] response = restTemplate.getForObject(serverUrl + "/readDataForState", State[].class);
        return Arrays.stream(response)
                .filter(e -> e.name.equals(state))
                .findFirst()
                .map(e -> e.abbreviation)
                .orElse("Found nothing similar to state " + state.toUpperCase());
    }

}
