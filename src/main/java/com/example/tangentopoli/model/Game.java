package com.example.tangentopoli.model;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.exception.BoxAlreadyPurchasedException;
import com.example.tangentopoli.exception.NotEnoughMoneyException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.example.tangentopoli.exception.MaximumConstructionOnProperty;
import com.example.tangentopoli.exception.NotYourPropertyException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Game implements Serializable {
    private Board board;
    private ArrayList<Player> players;
    private ArrayList<CommunityChest> communityChestCards = new ArrayList<>();
    private ArrayList<Chance> chanceCards = new ArrayList<>();
    private int turn = 0;
    private int playerInTurn=0;
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
    public ArrayList<CommunityChest> getCommunityChestCards() {
        return communityChestCards;
    }

    public void setCommunityChestCards(ArrayList<CommunityChest> communityChestCards) {
        this.communityChestCards = communityChestCards;
    }

    // Getter e Setter per chanceCards
    public ArrayList<Chance> getChanceCards() {
        return chanceCards;
    }

    public void setChanceCards(ArrayList<Chance> chanceCards) {
        this.chanceCards = chanceCards;
    }

    // Getter e Setter per turn
    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    // Getter e Setter per playerInTurn
    public int getPlayerInTurn() {
        return playerInTurn;
    }

    public void setPlayerInTurn(int playerInTurn) {
        this.playerInTurn = playerInTurn;
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