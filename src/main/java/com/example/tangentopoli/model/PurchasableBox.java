package com.example.tangentopoli.model;

public class PurchasableBox implements Box {
    private String name;
    private int price;

    public PurchasableBox(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String getName() {
        return name;
    }
}
