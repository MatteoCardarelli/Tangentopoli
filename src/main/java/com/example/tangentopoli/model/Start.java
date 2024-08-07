package com.example.tangentopoli.model;

public class Start implements Box{
    private String name;

    public Start() {
        this.name = "Start";
    }

    @Override
    public String getName() {
        return name;
    }
}
