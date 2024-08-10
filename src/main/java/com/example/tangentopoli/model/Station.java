package com.example.tangentopoli.model;

public class Station extends PurchasableBox {
    private int rentPrice;

    public Station(String name, int price, int transitPrice, int mortgageValue) {
        super(name, price, mortgageValue);
    }

    @Override
    public int calculatePrice() {
        return rentPrice;
    }
}
