package com.example.tangentopoli.model;

import com.example.tangentopoli.exception.NotEnoughMoneyException;

import java.util.ArrayList;

public abstract class Player {
    private int playerID;
    private int rowPos = 0;
    private int columnPos = 0;
    private int money;
    private ArrayList<PurchasableBox> purchasedBoxes = new ArrayList<>();

    public Player(int playerID, int money) {
        this.playerID = playerID;
        this.money = money;
    }

    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }

    public int getRowPos() {
        return rowPos;
    }

    public void setColumnPos(int columnPos) {
        this.columnPos = columnPos;
    }

    public int getColumnPos() {
        return columnPos;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return this.money;
    }

    public void buyPurchasableBox(PurchasableBox purchasableBox) throws NotEnoughMoneyException {
        if (purchasedBoxes.getPrice(purchasableBox) > this.getMoney()) {
            purchasedBoxes.add(purchasableBox);
        }

    }
}
