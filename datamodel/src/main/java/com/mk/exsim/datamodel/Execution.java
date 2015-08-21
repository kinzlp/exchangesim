package com.mk.exsim.datamodel;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Created by macbookpro on 8/18/2015.
 */
@Immutable
public class Execution {
    private final String symbol;
    private final int quantity;
    private final OrderSide side;
    private final double price;
    private final String id;
    private final String exchangeId;

    public Execution(String symbol, int quantity, OrderSide side, double price, String id, String exchangeId) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.side = side;
        this.price = price;
        this.id = id;
        this.exchangeId = exchangeId;
    }
}
