package com.example.tangentopoli.model;

public class Society extends PurchasableBox{
    private int transitPrice;

    public Society(String name, int price, int transitPrice, int mortgageValue) {
        super(name, price, mortgageValue);
    }

    @Override
    public void calculatePrice() {

    }
}
