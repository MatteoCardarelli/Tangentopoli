package com.example.tangentopoli.model;


public class CardBox implements Box {
    private String name;

    public CardBox(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
