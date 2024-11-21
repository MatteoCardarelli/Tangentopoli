package com.example.tangentopoli.model;

import com.example.tangentopoli.service.ChanceService;
import com.example.tangentopoli.service.CommunityChestService;

import java.io.Serializable;
import java.util.ArrayList;


public class Game implements Serializable {
    private Board board;
    private ArrayList<Player> players;
    private ArrayList<CommunityChestService> communityChestServiceCards = new ArrayList<>();
    private ArrayList<ChanceService> chanceServiceCards = new ArrayList<>();
    private int turn = 0;
    private int playerInTurnIndex =0;
    private int diceEqualsCombo = 0;
    private boolean exitPrisonFlag = false;


    /*//FAI SINGLETON
    public Game(int playersN, boolean CPUPlayers){
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        board = new Board(playersN);

        createPlayers(playersN, CPUPlayers);
        loadCards("src/main/resources/serialization/cards/chances.json", "src/main/resources/serialization/cards/communityChests.json");
        System.out.println(communityChestCards);
    }*/


    // Getter e Setter per Board
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    // Getter e Setter per players
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    // Getter e Setter per communityChestCards
    public ArrayList<CommunityChestService> getCommunityChestCards() {
        return communityChestServiceCards;
    }

    public void setCommunityChestCards(ArrayList<CommunityChestService> communityChestServiceCards) {
        this.communityChestServiceCards = communityChestServiceCards;
    }

    // Getter e Setter per chanceCards
    public ArrayList<ChanceService> getChanceCards() {
        return chanceServiceCards;
    }

    public void setChanceCards(ArrayList<ChanceService> chanceServiceCards) {
        this.chanceServiceCards = chanceServiceCards;
    }

    // Getter e Setter per turn
    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    // Getter e Setter per playerInTurn
    public int getPlayerInTurnIndex() {
        return playerInTurnIndex;
    }

    public Player getPlayerInTurn() {
        return players.get(playerInTurnIndex);
    }

    public void setPlayerInTurnIndex(int playerInTurnIndex) {
        this.playerInTurnIndex = playerInTurnIndex;
    }

    // Getter e Setter per diceEqualsCombo
    public int getDiceEqualsCombo() {
        return diceEqualsCombo;
    }

    public void setDiceEqualsCombo(int diceEqualsCombo) {
        this.diceEqualsCombo = diceEqualsCombo;
    }

    // Getter e Setter per exitPrisonFlag
    public boolean isExitPrisonFlag() {
        return exitPrisonFlag;
    }

    public void setExitPrisonFlag(boolean exitPrisonFlag) {
        this.exitPrisonFlag = exitPrisonFlag;
    }

}