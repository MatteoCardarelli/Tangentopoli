package com.example.tangentopoli.model;

import java.util.ArrayList;

public class Game {
    private Board board;
    private ArrayList<Player> players;
    private Deck communityChests;
    private Deck chanches;
    private Dice dice;
    private int turn = 0;
    private int playerInTurn=0;

    //FAI SINGLETON
    public Game(int playersN){
        board = new Board(playersN);
        createPlayers(playersN);
    }

    //Populates players list, assigning to each player an ID and the initial amount of money
    private void createPlayers(int playersN) {
        players = new ArrayList<>(playersN);
        for (int i = 0; i < playersN; i++) {
            HumanPlayer player = new HumanPlayer(i+1, 1500);
            players.add(player);
            player.setPosition(board.getBoxFromIndex(0));   //Set player to start position
        }
    }

    public int getPlayersN() {
        return players.size();
    }

    public void changeTurn() {
        playerInTurn = (playerInTurn + 1) % players.size();
        turn++;
    }

    public Player getPlayerInTurn() {
        return players.get(playerInTurn);
    }

    public int getPlayerTurn() { return playerInTurn; }

    public void goToPrison(Player player) {
        player.setInPrison(true);
        player.setPosition(board.getBoxes().get((board.getSize() / 4)));
    }

    public void movePlayer(int diceValue) {
        Player player = getPlayerInTurn();

        //il giocatore non può muoversi
        if (player.isInPrison()) return;

        Box currentPosition = player.getPosition();
        int currentPosIndex = board.getBoxIndex(currentPosition);
        int nextPosIndex = currentPosIndex + diceValue;
        Box nextPos = board.getBoxFromIndex(nextPosIndex);
        int nextPosIndexMod = board.getBoxIndex(nextPos);

        //Once the player passes on the START the game gives him 200€
        if(nextPosIndexMod < currentPosIndex) {
            player.setMoney(player.getMoney() + 200);
        }
        if(nextPos.getName().equals("Prigione"))
            goToPrison(player);
        else
            player.setPosition(nextPos);
    }
}


