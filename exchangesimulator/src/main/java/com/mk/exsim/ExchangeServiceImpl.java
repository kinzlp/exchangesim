package com.mk.exsim;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan
public class ExchangeServiceImpl implements IExchangeService {

    public void init() throws InterruptedException {
        Thread.sleep(1000);
    }
}
