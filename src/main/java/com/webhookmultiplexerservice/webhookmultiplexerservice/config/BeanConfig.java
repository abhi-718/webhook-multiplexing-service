package com.webhookmultiplexerservice.webhookmultiplexerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Bean(name = "restTemplate1")
    public RestTemplate restTemplate1() {
        return new RestTemplate(getClientHttpRequestFactory(1_000));
    }

    private static ClientHttpRequestFactory getClientHttpRequestFactory(int timeout) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
                = new SimpleClientHttpRequestFactory();
        //Connect timeout
        clientHttpRequestFactory.setConnectTimeout(timeout);

        //Read timeout
        clientHttpRequestFactory.setReadTimeout(timeout);
        return clientHttpRequestFactory;
    }
}