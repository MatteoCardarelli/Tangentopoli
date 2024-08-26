package com.example.tangentopoli.model;

public class Chance extends Card {
    public Chance(String title, String description, int type, int moneyAmount) {
        super(title, description, type, moneyAmount);
    }

    public Chance(){
        super();
    };

    @Override
    public void action(Player player) {

    }
}
