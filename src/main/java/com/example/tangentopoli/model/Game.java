package com.example.tangentopoli.model;

import java.util.ArrayList;

public class Game {
    private Board board;
    ArrayList<Player> players;
    Deck communityChests;
    Deck chanches;

    //FAI SINGLETON
    public Game(int playersN){
        board = new Board(playersN);
    }
}
