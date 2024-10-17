package com.example.tangentopoli.model;

import com.example.tangentopoli.service.PlayerService;

import java.io.Serializable;

public abstract class PurchasableBox implements Box, Serializable {
    private String name;
    private int price;
    private boolean purchased = false;
    private Player owner;
    private int mortgageValue;
    private boolean isMortgaged = false;

    public PurchasableBox(String name, int price, int mortgageValue) {
        this.name = name;
        this.price = price;
        this.mortgageValue = mortgageValue;
    }

    public int getPrice() {
        return price;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        String str = this.name + "    Costo: " + this.price + "    Val ipotecario: " + this.mortgageValue;
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

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public Player getOwner() {return owner;}

    public void setOwner(Player owner) {this.owner = owner;}

    public abstract int calculatePrice(PlayerService playerService);

    // Restituisce se la proprietà è ipotecata
    public boolean isMortgaged() {
        return isMortgaged;
    }

    public boolean isMortgagable() {
        return !isMortgaged;
    }

    // Imposta lo stato di ipoteca della proprietà
    public void setMortgaged(boolean mortgaged) {
        isMortgaged = mortgaged;
    }


}

