package com.example.tangentopoli.model;

import java.util.Random;

public class Dice {


    //this method returns a random value from 1 to 12
    public static int getRandomNu() {
        Random random = new Random();
        return random.nextInt(12) + 1;
    }
}
