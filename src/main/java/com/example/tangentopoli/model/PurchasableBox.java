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

    @Override
    public String getDescription() {
        String str = this.name + " \nCosto: " + this.price + " \nVal ipotecario: " + this.mortgageValue;
        return str;
    }

    public boolean isStation() {
        return (this instanceof Station);
    }

    public boolean isProperty() {
        return (this instanceof Property);
    }

    public boolean isSociety() {
        return (this instanceof Society);
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setAsPurchased() {
        purchased = true;
    }

    public Player getOwner() {return owner;}

    public void setOwner(Player owner) {this.owner = owner;}

    public abstract int calculatePrice();

}
