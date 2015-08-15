package com.mk.exchangesim;

import org.springframework.stereotype.Component;

@Component
public class ExchangeService {

    public void init() throws InterruptedException {
        Thread.sleep(100000);
    }
}
