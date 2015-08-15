package com.mk.exchangesim;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExchangeSimMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        ExchangeService exchangeService = context.getBean(ExchangeService.class);
        try {
            exchangeService.init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
