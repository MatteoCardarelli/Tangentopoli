package com.example.tangentopoli.model;


import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.Random;

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

    public void moveBot() {

        // Logica per tirare i dadi con un ritardo
        PauseTransition delay = new PauseTransition(Duration.seconds(1)); // 1 secondo di ritardo
        delay.setOnFinished(event -> {

            //se il bot è in prigione e ha i soldi per uscire lo fa
            if (isInPrison() && getMoney() > 50) game.getController().exitFromPrison();

            game.getController().launchDices();
        });
        delay.play();
    }

    public void takeStrategicDecisions() {
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5)); // Ritardo di 500ms
        delay.setOnFinished(event -> {

            // Acquisto proprietà se disponibile
            if (getPosition() instanceof PurchasableBox) {
                game.getController().buyCurrentBox();
            }

           /*
           //costruisce sulle proprietà
            int buildableProperiesSize = game.getBuildableProperties().size();
            try {
                game.buildOnProperty(new Random().nextInt(buildableProperiesSize));
            } catch (MaximumConstructionOnProperty | NotEnoughMoneyException e) {
                e.printStackTrace();
            }
            if(this.getMoney() < 200) {
                int mortgageableBoxessSize = game.getMortgageableBoxes().size();
                game.mortgageBox(new Random().nextInt(mortgageableBoxessSize));
            }
            if (this.getMoney() < 200) {
                int demolishablePropertiesSize = game.getDemolishableProperties().size();
                game.demolishOnProperty(new Random().nextInt(demolishablePropertiesSize));
            }


            */







            if (canLaunchAgain) {
                moveBot();
                PauseTransition secondDelay = new PauseTransition(Duration.seconds(1.5)); // Ritardo di 1.5 secondi
                secondDelay.setOnFinished(e -> {
                    canLaunchAgain = false;
                });
                secondDelay.play();
            } else {
                // Cambio turno dopo un ritardo
                PauseTransition nextTurnDelay = new PauseTransition(Duration.seconds(0.5)); // Ritardo di 500ms
                nextTurnDelay.setOnFinished(e -> game.getController().nextTurn());
                nextTurnDelay.play();
            }
        });
        delay.play();
    }
}


