package com.example.tangentopoli.model;

public class Property extends PurchasableBox {
    private int mortgageValue;
    private int houseN = 0;
    private int hotel = 0;
    private int rentPrice;

    public Property(String name, int price, int rentPrice) {
        super(name, price);
        this.rentPrice = rentPrice;
    }
}
