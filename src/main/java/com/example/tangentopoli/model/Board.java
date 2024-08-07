package com.example.tangentopoli.model;

import java.util.ArrayList;

public class Board {
    private ArrayList<Box> boxes = new ArrayList<>();

    public Board(int playerN) {
        switch (playerN) {
            case 2, 3:
                //Genera tutto
                break;
            case 4,5:

                break;
            case 6:

                break;
            default:
                break;
        }
    }
}
