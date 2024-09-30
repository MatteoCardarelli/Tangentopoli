package com.example.tangentopoli.model;


import java.io.Serializable;

public class CardBox implements Box, Serializable {
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
        return this.name;
    }
}
