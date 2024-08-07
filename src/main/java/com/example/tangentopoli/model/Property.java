package com.example.tangentopoli.model;

public class Property extends PurchasableBox {
    private int houseN = 0;
    private int hotel = 0;
    private int rentPrice;
    private int mortgageValue;

    public Property(String name, int price, int rentPrice, int mortgageValue) {
        super(name, price);
        this.rentPrice = rentPrice;
    }

    @Override
    public void calculatePrice() {

    }
}
