package com.example.tangentopoli.model;

import java.io.Serializable;

public class Tax implements Box, Serializable {
    private String name;
    private int price;

    public Tax(String name, int price) {
        this.name = name;
        this.price = price;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return this.name + " " + this.price;
    }

    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }
}
