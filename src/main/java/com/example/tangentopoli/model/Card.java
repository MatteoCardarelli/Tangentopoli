package com.example.tangentopoli.model;

import com.example.tangentopoli.exception.NotEnoughMoneyException;

public abstract class Card {
    private String title;
    private String description;
    private int type;
    private int moneyAmount;

    public Card(String title, String description, int type, int moneyAmount) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.moneyAmount = moneyAmount;
    }

    public Card() {

    }

    public abstract void action(Game game) throws NotEnoughMoneyException;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(int moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String toString() {
        String str = this.title + "\t" + this.description;
        return str;
    }

}
