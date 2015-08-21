package com.mk.exsim.datamodel;

/**
 * Created by macbookpro on 8/18/2015.
 */
public enum OrderSide {
    BUY("Buy"), SELL("Sell"), SHORT_SELL("Short Sell"), SHORT_SELL_EXEMPT("Short Sell Exempt");

    private final String name;

    OrderSide(String name) {
        this.name = name;
    }
}
