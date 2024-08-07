package com.example.tangentopoli.model;

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

    //Create PurchasedBoxException
    public void buyPurchasableBox(PurchasableBox purchasableBox) throws NotEnoughMoneyException {
        if (!purchasableBox.isPurchased() && purchasableBox.getPrice() > this.getMoney()) {
            purchasedBoxes.add(purchasableBox);
            purchasableBox.setAsPurchased();
        }

    }
}
