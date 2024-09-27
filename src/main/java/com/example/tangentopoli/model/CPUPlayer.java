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
            if (this.getMoney() > 500) {
                if (getPosition() instanceof PurchasableBox) {
                    game.getController().buyCurrentBox();
                }
            }


            //se il bot ha meno di 200€ ipoteca una proprietà casuale
            if(this.getMoney() < 200) {
                int mortgageableBoxessSize = game.getMortgageableBoxes().size();
                if (mortgageableBoxessSize != 0) {
                    Integer propertyIndex = game.getMortgageableBoxes().get(new Random().nextInt(mortgageableBoxessSize));
                    int[] propertyCoords = game.getController().convertPositionToGridCoords(propertyIndex.intValue());
                    game.getController().mortgageBox(propertyCoords[0], propertyCoords[1]);

                }
            }

            //se dopo aver ipotecato ha ancora sotto i 200€ demolisci una propietà casuale
            if (this.getMoney() < 200) {
                int demolishablePropertiesSize = game.getDemolishableProperties().size();
                if (demolishablePropertiesSize != 0) {
                    Integer propertyIndex = game.getDemolishableProperties().get(new Random().nextInt(demolishablePropertiesSize));
                    int[] propertyCoords = game.getController().convertPositionToGridCoords(propertyIndex.intValue());
                    game.getController().demolishOnProperty(propertyCoords[0], propertyCoords[1]);

                }
            }

            //se ha più di 700€ e delle propietà ipotecate estingue l'ipoteca
            if (this.getMoney() > 700) {
                int extinguishablePropertiesSize = game.getExtinguishableBoxes().size();
                if (extinguishablePropertiesSize != 0) {
                    Integer propertyIndex = game.getExtinguishableBoxes().get(new Random().nextInt(extinguishablePropertiesSize));
                    int[] propertyCoords = game.getController().convertPositionToGridCoords(propertyIndex.intValue());
                    game.getController().extinguishMortgageBox(propertyCoords[0], propertyCoords[1]);
                }
            }

            //costruisce sulle proprietà
            if (this.getMoney() > 700) {}
            int buildableProperiesSize = game.getBuildableProperties().size();
            if (buildableProperiesSize != 0) {
                Integer propertyIndex = game.getBuildableProperties().get(new Random().nextInt(buildableProperiesSize));
                int[] propertyCoords = game.getController().convertPositionToGridCoords(propertyIndex.intValue());
                game.getController().buildOnProperty(propertyCoords[0], propertyCoords[1]);
            }





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


