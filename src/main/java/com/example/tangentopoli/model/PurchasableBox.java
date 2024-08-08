package com.example.tangentopoli.model;

public abstract class PurchasableBox implements Box {
    private String name;
    private int price;
    private boolean purchased = false;
    private Player owner;
    private int mortgageValue;

    public PurchasableBox(String name, int price, int mortgageValue) {
        this.name = name;
        this.price = price;
        this.mortgageValue = mortgageValue;


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

    public Player getOwner() {return owner;}

    public void setOwner(Player owner) {this.owner = owner;}

    public abstract void calculatePrice();

}
