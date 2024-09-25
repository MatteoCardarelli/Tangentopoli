package com.example.tangentopoli.model;


public class CPUPlayer extends Player {

    private Game game;
    private boolean canLaunchAgain = false;

    public CPUPlayer(int playerID, int money, Game game) {
        super(playerID, money);
        this.game = game;
    }

    public void setCanLaunchAgain(boolean canLaunchAgain){
        this.canLaunchAgain = canLaunchAgain;
    }

    public void moveBot(){
        // Logica per tirare i dadi
        game.getController().launchDices();
    }

    public void takeStrategicDecisions(){
        if (getPosition() instanceof PurchasableBox) {
            game.getController().buyCurrentBox();

            // Aggiungi un ritardo per permettere alla GUI di aggiornarsi
            try {
                Thread.sleep(500); // Ritardo di 500 millisecondi (mezzo secondo)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // In caso di interruzione, reimposta lo stato del thread
            }
        }

        if (canLaunchAgain) {
            moveBot();

            // Aggiungi un altro ritardo prima della prossima azione
            try {
                Thread.sleep(1500); // Ritardo di 500 millisecondi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            canLaunchAgain = false;
        } else {
            game.getController().nextTurn();

            // Ritardo prima del cambio di turno, se necessario
            try {
                Thread.sleep(500); // Ritardo di 500 millisecondi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
