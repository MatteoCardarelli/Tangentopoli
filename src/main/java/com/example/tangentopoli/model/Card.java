package com.example.tangentopoli.model;

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

    public abstract void action();
}
