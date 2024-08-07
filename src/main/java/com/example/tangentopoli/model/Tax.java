package com.example.tangentopoli.model;

public class Tax implements Box {
    private String name;

    public Tax() {
        this.name = "Prison";
    }

    @Override
    public String getName() {
        return name;
    }
}
