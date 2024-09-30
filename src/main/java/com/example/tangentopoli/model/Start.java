package com.example.tangentopoli.model;

import java.io.Serializable;

public class Start implements Box, Serializable {
    private String name;

    public Start() {
        this.name = "Start";
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
