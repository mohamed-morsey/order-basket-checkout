package com.elmenus.order.basket.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableWebFlux
public class OrderBasketCheckoutApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderBasketCheckoutApplication.class, args);
    }
}
