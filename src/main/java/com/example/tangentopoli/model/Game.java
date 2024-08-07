package com.example.tangentopoli.model;

import java.util.ArrayList;

public class Game {
    private Board board;
    private ArrayList<Player> players;
    private Deck communityChests;
    private Deck chanches;
    private Dice dice;

    //FAI SINGLETON
    public Game(int playersN){
        board = new Board(playersN);
    }
}
