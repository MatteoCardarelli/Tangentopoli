package com.example.tangentopoli.model;

public class Property extends PurchasableBox {
    private int houseN = 0;
    private int hotel = 0;
    private int rentPrice;
    private String color;


    public Property(String name, int price, int rentPrice, int mortgageValue, String color) {
        super(name, price, mortgageValue);
        this.rentPrice = rentPrice;
        this.color = color;
    }

    @Override
    public int calculatePrice() {
        return rentPrice;
    }
}
