package com.example.tangentopoli.model;

public class Station extends PurchasableBox {
    private int transitPrice;
    private int mortgageValue;

    public Station(String name, int price, int transitPrice, int mortgageValue) {
        super(name, price);
    }

    @Override
    public void calculatePrice() {

    }
}
