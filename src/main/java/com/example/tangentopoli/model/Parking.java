package com.example.tangentopoli.model;

public class Parking implements Box {
    private String name;

    public Parking() {
        name = "Parcheggio";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return this.name;
    }
}
