package com.example.tangentopoli.model;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable {


    //this method returns a random value from 1 to 12
    public static int launch() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}
