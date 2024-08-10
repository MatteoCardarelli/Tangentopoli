package com.example.tangentopoli.model;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.exception.BoxAlreadyPurchasedException;
import com.example.tangentopoli.exception.NotEnoughMoneyException;

import java.util.ArrayList;

public abstract class Player {
    private int playerID;
    private Box position;
    private int money;
    private int launchValue;
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

    public int getLaunchValue() {
        return this.launchValue;
    }

    public void setPosition(Box position) {
        this.position = position;
    }

    public String toString() {
        String str = "Giocatore " + this.playerID + " | Patrimonio: " + this.money + "€\n";
        for(PurchasableBox box : this.purchasedBoxes) {
            str += "- " + box.getName() + "\n";
        }

        return str;
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

    public void payRent(BoardController boardController) throws NotEnoughMoneyException {
        // Ottieni la casella su cui si trova il giocatore
        Box currentBox = this.getPosition();
        launchValue = boardController.getDiceValue();

        // Controlla se la casella corrente è una proprietà acquistabile e se è stata acquistata da un altro giocatore
        if (currentBox instanceof PurchasableBox purchasableBox && purchasableBox.isPurchased()) {
            Player owner = purchasableBox.getOwner();

            // Controlla se il proprietario non è il giocatore corrente (per evitare di pagare a se stessi)
            if (!owner.equals(this)) {
                int rentToPay = purchasableBox.calculatePrice();

                // Il giocatore corrente paga il proprietario
                try {
                    this.setMoney(this.getMoney() - rentToPay);
                    owner.setMoney(owner.getMoney() + rentToPay);

                    // Aggiorna le informazioni del giocatore e della casella
                    boardController.displayPlayerInfo(this);
                    boardController.displayPlayerInfo(owner);
                    boardController.updateBoxInfo("Hai pagato " + rentToPay + "€ di affitto a Player" + owner.getPlayerID() + " per la proprietà " + purchasableBox.getName());
                } catch (Exception e) {
                    // Gestisci eventuali eccezioni come NotEnoughMoneyException se desiderato
                    boardController.updateBoxInfo("Non hai abbastanza denaro per pagare l'affitto.");
                }
            }
        }
    }

    public int countOwnedStations() {
        int count = 0;
        for(PurchasableBox b : purchasedBoxes) {
            if(b.isStation())
                count++;
        }
        return count;
    }

    public int countOwnedSocieties() {
        int count = 0;
        for(PurchasableBox b : purchasedBoxes) {
            if(b.isSociety())
                count++;
        }
        return count;
    }
}


