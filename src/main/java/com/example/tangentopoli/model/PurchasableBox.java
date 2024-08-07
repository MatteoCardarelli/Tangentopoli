package com.example.tangentopoli.model;

public abstract class PurchasableBox implements Box {
    private String name;
    private int price;
    private boolean purchased = false;

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

    public boolean isPurchased() {
        return purchased;
    }

    public void setAsPurchased() {
        purchased = true;
    }

    public abstract void calculatePrice();
}
