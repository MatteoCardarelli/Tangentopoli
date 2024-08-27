package com.example.tangentopoli.model;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.exception.BoxAlreadyPurchasedException;
import com.example.tangentopoli.exception.MaximumConstructionOnProperty;
import com.example.tangentopoli.exception.NotEnoughMoneyException;
import com.example.tangentopoli.exception.NotYourPropertyException;

import java.util.ArrayList;

public abstract class Player {
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


    //costruttore
    public Player(int playerID, int money) {
        this.playerID = playerID;
        this.money = money;
    }

    //metodo per restituire il playerID
    public int getPlayerID() {
        return this.playerID;
    }

    //metodo per settare i soldi
    public void setMoney(int money) {
        this.money = money;
    }

    //metodo per restituire i soldi
    public int getMoney() {
        return this.money;
    }

    public ArrayList<PurchasableBox> getPurchasedBoxes() {
        return this.purchasedBoxes;
    }

    //metodo per settare un giocatore in prigione
    public void setInPrison(boolean inPrison) {
        this.inPrison = inPrison;
        this.turnInPrison = 0;
    }

    //metodo per controllare se un giocatore è in prigione
    public boolean isInPrison() {
        return this.inPrison;
    }

    //metodo per settare da quant turni un giocatore è in prigione
    public void setTurnInPrison() {
        this.turnInPrison = ++turnInPrison % 4;
        System.out.println("turno in prigione di player " + getPlayerID() + " " +  turnInPrison);
    }

    //metodo per restituire da quanti turni il giocatore è in prigione
    public int getTurnInPrison(){
        return turnInPrison;
    }

    //metodo per uscire di prigione pagando
    public void exitFromPrison()  {
        if(this.isExitFromPrisonCard()) {
            this.setInPrison(false);
            this.setExitFromPrisonCard(false);
        } else {
            this.setMoney(this.getMoney()-50);
            this.setInPrison(false);
        }
    }

    //metodo per settare la posizione
    public void setPosition(Box position) {
        this.position = position;
    }

    //metodo per restituire la posizione
    public Box getPosition() {
        return this.position;
    }

    //da eliminare a gioco completo
    public int getLaunchValue() {
        return this.launchValue;
    }

    public boolean isSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    public boolean isExitFromPrisonCard() {
        return exitFromPrisonCard;
    }

    public void setExitFromPrisonCard(boolean exitFromPrisonCard) {
        this.exitFromPrisonCard = exitFromPrisonCard;
    }

    //metodo per acquistare caselle
    public void buyPurchasableBox(PurchasableBox purchasableBox) throws NotEnoughMoneyException, BoxAlreadyPurchasedException {
        if (!purchasableBox.isPurchased() && purchasableBox.getPrice() <= this.getMoney()) {
            this.setMoney(this.getMoney()-purchasableBox.getPrice());
            purchasedBoxes.add(purchasableBox);
            purchasableBox.setPurchased(true);
            purchasableBox.setOwner(this);
        }
        else if (purchasableBox.getPrice() > this.getMoney()) {
            throw new NotEnoughMoneyException("Non hai abbastanza denaro per acquistare questa proprietà");
        }
        else if(purchasableBox.isPurchased()) {
            throw new BoxAlreadyPurchasedException("Questa proprietà è già stata acquistata");
        }

    }

    //metodo per pagare l'affitto
    public void payRent(BoardController boardController) {
        // Ottieni la casella su cui si trova il giocatore
        Box currentBox = this.getPosition();

        //da eliminare a fine gioco
        launchValue = boardController.getDiceValue();
        System.out.println(getPlayerID()+" "+launchValue);


        // Controlla se la casella corrente è una proprietà acquistabile e se è stata acquistata da un altro giocatore
        if (currentBox instanceof PurchasableBox purchasableBox && purchasableBox.isPurchased()) {
            Player owner = purchasableBox.getOwner();

            // Controlla se il proprietario non è il giocatore corrente (per evitare di pagare a se stessi) e verifica che non sia ipotecata
            if (!owner.equals(this) && !((PurchasableBox) currentBox).isMortgaged()) {
                int rentToPay = purchasableBox.calculatePrice();

                // Il giocatore corrente paga il proprietario
                this.setMoney(this.getMoney() - rentToPay);
                owner.setMoney(owner.getMoney() + rentToPay);

                // Aggiorna le informazioni del giocatore e della casella
                boardController.displayPlayerInfo(this);
                boardController.updateBoxInfo("Hai pagato " + rentToPay + "€ di affitto a Player" + owner.getPlayerID() + " per la proprietà " + purchasableBox.getName());
            }
        }
    }

    public void payTax(BoardController boardController) {
        // Ottieni la casella su cui si trova il giocatore
        Box currentBox = this.getPosition();

        // Controlla se la casella corrente è una casella tasse
        if (currentBox instanceof Tax taxBox) {
            int taxAmount = taxBox.getPrice();

            // Sottrai l'importo delle tasse dal denaro del giocatore
            this.setMoney(this.getMoney() - taxAmount);

            // Aggiorna le informazioni del giocatore e della casella
            boardController.displayPlayerInfo(this);
            boardController.updateBoxInfo("Hai pagato " + taxAmount + "€ di tasse per la casella " + taxBox.getName());
        }
    }

    //metodo per contare le stazioni di un giocatore
    public int countOwnedStations() {
        int count = 0;
        for(PurchasableBox b : purchasedBoxes) {
            if(b.isStation())
                count++;
        }
        return count;
    }

    //metodo per contare le società di un giocatore
    public int countOwnedSocieties() {
        int count = 0;
        for(PurchasableBox b : purchasedBoxes) {
            if(b.isSociety())
                count++;
        }
        System.out.println("N" + count);
        return count;
    }

    public void buildOnProperty(Property property) throws NotYourPropertyException, MaximumConstructionOnProperty, NotEnoughMoneyException {
        if(!(purchasedBoxes.contains(property)))
            throw new NotYourPropertyException();
        else {
            int price = property.getCostructionPrice();
            if(price < this.getMoney()) {
                property.addConstruction();
                this.money -= price;
            }
            else throw new NotEnoughMoneyException("Non hai abbastanza soldi");
        }
    }

    /*public boolean canMortgage(PurchasableBox box) {
        // Controlla se la casella è una proprietà
        if (box instanceof Property property) {
            // Verifica che la proprietà non abbia case o hotel costruiti sopra
            return property.getHouseN() == 0 && property.getHotel() == 0 && !property.isMortgaged();
        }
        // Le stazioni e le società possono essere ipotecate se non sono già ipotecate
        return !box.isMortgaged();
    }*/

    public void mortgageBox(PurchasableBox box) {
        box.setMortgaged(true);
        this.money += box.getMortgageValue();
        purchasedBoxes.remove(box);
        mortgagedBoxes.add(box);
    }

    // Verifica se il giocatore può rimuovere l'ipoteca su una proprietà
    public boolean canExtinguishMortgage(PurchasableBox purchasableBox) {
        if (purchasableBox.isMortgaged()) {
            int mortgageRemovalCost = purchasableBox.getMortgageValue() + (int)(purchasableBox.getMortgageValue() * 0.1);
            return this.getMoney() >= mortgageRemovalCost;
        }
        return false;
    }

    public void extinguishMortgageBox(PurchasableBox purchasableBox) {
        purchasableBox.setMortgaged(false);
        this.money -= (purchasableBox.getMortgageValue() + (int)(purchasableBox.getMortgageValue() * 0.1));
        purchasedBoxes.add(purchasableBox);
        mortgagedBoxes.remove(purchasableBox);
    }

    public boolean canDemolish(Property property) {
        if(property.getHouseN() == 0 && property.getHotel() == 0)
            return false;
        else
            return true;
    }

    //metodo che restituisce la descrizione
    public String toString() {
        String str = "Giocatore " + this.playerID + " | Patrimonio: " + this.money + "€\n";
        for(PurchasableBox box : this.purchasedBoxes) {
            str += "- " + box.getName() + "\n";
        }

        for(PurchasableBox box : this.mortgagedBoxes) {
            str += "- " + box.getName() + " (ipoteca)\n";
        }

        return str;
    }

    public void demolishOnProperty(Property property) {
        int price = property.getDemolitionPrice();
        property.demolishConstruction();
        this.money += price;
    }

    public void hasLost() {
        for(PurchasableBox box : this.purchasedBoxes) {
            box.setOwner(null);
            box.setPurchased(false);
            if(box.isMortgaged())
                box.setMortgaged(false);
        }
        purchasedBoxes.clear();
    }

    public void pay(int moneyAmount) {
        this.money += moneyAmount;

    }

}