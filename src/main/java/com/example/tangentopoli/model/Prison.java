package com.example.tangentopoli.model;

import java.io.Serializable;

public class Prison implements Box, Serializable {
    private String name;

    public Prison() {
        name = "Prigione";
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
