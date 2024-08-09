package com.example.tangentopoli.model;

import com.example.tangentopoli.exception.BoxAlreadyPurchasedException;
import com.example.tangentopoli.exception.NotEnoughMoneyException;

import java.util.ArrayList;

public abstract class Player {
    private int playerID;
    private Box position;
    private int money;
    private ArrayList<PurchasableBox> purchasedBoxes = new ArrayList<>();

    public Player(int playerID, int money) {
        this.playerID = playerID;
        this.money = money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return this.money;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public Box getPosition() {
        return this.position;
    }

    public void setPosition(Box position) {
        this.position = position;
    }

    public String toString() {
        return "Player " + this.playerID + " | Money: " + this.money + "$\n"/* + purchasedBoxes.toString()*/;
    }

    public void buyPurchasableBox(PurchasableBox purchasableBox) throws NotEnoughMoneyException, BoxAlreadyPurchasedException {
        if (!purchasableBox.isPurchased() && purchasableBox.getPrice() <= this.getMoney()) {
            this.setMoney(this.getMoney()-purchasableBox.getPrice());
            purchasedBoxes.add(purchasableBox);
            purchasableBox.setAsPurchased();
            purchasableBox.setOwner(this);
        }
        else if (purchasableBox.getPrice() > this.getMoney()) {
            throw new NotEnoughMoneyException("Non hai abbastanza denaro per acquistare questa proprietà");
        }
        else if(purchasableBox.isPurchased()) {
            throw new BoxAlreadyPurchasedException();
        }

    }
}
