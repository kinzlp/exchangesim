package com.mk.exsim;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Exchange service impl
 */
@ComponentScan
public class ExchangeServiceImpl implements IExchangeService {

    /**
     * @throws InterruptedException
     */
    public void init() throws InterruptedException {
        Thread.sleep(1000);
    }
}
