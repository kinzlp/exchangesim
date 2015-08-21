package com.mk.exsim;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExchangeSimMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext_exsim.xml");
//        ExchangeServiceImpl exchangeServiceImpl = context.getBean(ExchangeServiceImpl.class);
//        try {
//            exchangeServiceImpl.init();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
