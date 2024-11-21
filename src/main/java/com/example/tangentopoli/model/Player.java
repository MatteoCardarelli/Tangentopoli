package com.example.tangentopoli.model;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.exception.BoxAlreadyPurchasedException;
import com.example.tangentopoli.exception.MaximumConstructionOnProperty;
import com.example.tangentopoli.exception.NotEnoughMoneyException;
import com.example.tangentopoli.exception.NotYourPropertyException;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Player implements Serializable {
    private int playerID;
    private Box position;
    private int money;
    private boolean skipTurn = false;
    private boolean exitFromPrisonCard = false;
    private boolean isInCrisis = false;
    private int crisisMoneyAmount;

    //da eliminare a fine gioco
    private int launchValue;

    private boolean inPrison = false;
    private int turnInPrison = 0;
    private ArrayList<PurchasableBox> purchasedBoxes = new ArrayList<>();
    private ArrayList<PurchasableBox> mortgagedBoxes = new ArrayList<>();
    private String playerImage = "/img/playerImages/";


    //costruttore
    public Player(int playerID, int money) {
        this.playerID = playerID;
        this.money = money;
        this.playerImage += "imgForPlayer" + playerID + ".png";
    }

    // Getter e Setter per playerID
    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    // Getter e Setter per position
    public Box getPosition() {
        return position;
    }

    public void setPosition(Box position) {
        this.position = position;
    }

    // Getter e Setter per money
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    // Getter e Setter per skipTurn
    public boolean isSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    // Getter e Setter per exitFromPrisonCard
    public boolean isExitFromPrisonCard() {
        return exitFromPrisonCard;
    }

    public void setExitFromPrisonCard(boolean exitFromPrisonCard) {
        this.exitFromPrisonCard = exitFromPrisonCard;
    }

    // Getter e Setter per isInCrisis
    public boolean isInCrisis() {
        return isInCrisis;
    }

    public void setInCrisis(boolean isInCrisis) {
        this.isInCrisis = isInCrisis;
    }

    // Getter e Setter per crisisMoneyAmount
    public int getCrisisMoneyAmount() {
        return crisisMoneyAmount;
    }

    public void setCrisisMoneyAmount(int crisisMoneyAmount) {
        this.crisisMoneyAmount = crisisMoneyAmount;
    }

    // Getter e Setter per launchValue (da eliminare a fine gioco)
    public int getLaunchValue() {
        return launchValue;
    }

    public void setLaunchValue(int launchValue) {
        this.launchValue = launchValue;
    }

    // Getter e Setter per inPrison
    public boolean isInPrison() {
        return inPrison;
    }

    public void setInPrison(boolean inPrison) {
        this.inPrison = inPrison;
    }

    // Getter e Setter per turnInPrison
    public int getTurnInPrison() {
        return turnInPrison;
    }

    public void setTurnInPrison(int turnInPrison) {
        this.turnInPrison = turnInPrison;
    }

    // Getter e Setter per purchasedBoxes
    public ArrayList<PurchasableBox> getPurchasedBoxes() {
        return purchasedBoxes;
    }

    public void setPurchasedBoxes(ArrayList<PurchasableBox> purchasedBoxes) {
        this.purchasedBoxes = purchasedBoxes;
    }

    // Getter e Setter per mortgagedBoxes
    public ArrayList<PurchasableBox> getMortgagedBoxes() {
        return mortgagedBoxes;
    }

    public void setMortgagedBoxes(ArrayList<PurchasableBox> mortgagedBoxes) {
        this.mortgagedBoxes = mortgagedBoxes;
    }

    public String getImagePath() {
        return this.playerImage;
    }

    public String[] toStringArray() {
        String[] strInfo = new String[2];
        strInfo[0] = "Giocatore " + playerID;
        strInfo[1] =  money + "€" ;
        return strInfo;
    }
}