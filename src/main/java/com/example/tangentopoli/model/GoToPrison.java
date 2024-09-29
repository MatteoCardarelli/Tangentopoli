package com.example.tangentopoli.model;

import java.io.Serializable;

public class GoToPrison implements Box, Serializable {
    @Override
    public String getName() {
        return "GoToPrison";
    }

    @Override
    public String getDescription() {
        return "Vai in prigione!";
    }
}
