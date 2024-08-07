package com.example.tangentopoli.model;

public class Society extends PurchasableBox{
    private int transitPrice;
    private int mortgageValue;

    public Society(String name, int price, int transitPrice, int mortgageValue) {
        super(name, price);
    }

    @Override
    public void calculatePrice() {

    }
}
