package com.example.tangentopoli.model;

public class Prison implements Box {
    private String name;

    public Prison() {
        name = "Prison";
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
