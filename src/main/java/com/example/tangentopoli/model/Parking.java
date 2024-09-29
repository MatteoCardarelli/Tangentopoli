package com.example.tangentopoli.model;

import java.io.Serializable;

public class Parking implements Box, Serializable {
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
