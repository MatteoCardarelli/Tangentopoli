package com.example.tangentopoli.controller;

import com.example.tangentopoli.service.AuctionService;
import com.example.tangentopoli.service.PlayerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;

public class AuctionController implements Initializable {

    private AuctionService auctionService;

    @FXML
    AnchorPane auctionWindow;

    @FXML
    Label maxBidCounter;

    @FXML
    ImageView imageOfPropertyOnAuctiom;

    @FXML
    Label cashOfPlayer;

    @FXML
    Label winnerLabel;

    @FXML
    ImageView currentPlayerImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       // imageOfPropertyOnAuctiom.setImage();
    }

    public void initialize(AuctionService auctionService) {
        this.auctionService = auctionService;
        updateUIForCurrentPlayer();
    }

    // Metodo per avanzare al prossimo turno
    public void nextTurn() {
        updateUIForCurrentPlayer();
    }


    // Aggiorna l'interfaccia per il giocatore corrente
    private void updateUIForCurrentPlayer() {
        PlayerService currentPlayer = auctionService.getCurrentPlayer();

        // Aggiorna la puntata corrente
        maxBidCounter.setText("" + auctionService.getCurrentBid());

        // Aggiorna il denaro del giocatore corrente
        cashOfPlayer.setText("" + currentPlayer.getMoney());

        // Aggiorna l'immagine del giocatore
        Image playerImage = new Image(getClass().getResourceAsStream(currentPlayer.getImagePath()));

        // Assicurati che l'ImageView abbia larghezza e altezza definite
        double width = currentPlayerImageView.getFitWidth();
        double height = currentPlayerImageView.getFitHeight();
        currentPlayerImageView.setPreserveRatio(false);

        // Crea un cerchio centrato sull'immagine
        Circle clip = new Circle(width / 2, height / 2, (Math.min(width, height) / 2));
        currentPlayerImageView.setScaleX(1.1);
        currentPlayerImageView.setScaleY(1.1);


        currentPlayerImageView.setImage(playerImage);
        currentPlayerImageView.setClip(clip);
    }

    // Metodo di offerta per +1
    public void handleBidPlus1() {
        maxBidCounter.setText(maxBidCounter.getText()+1);
        auctionService.placeFixedBid(1);
        nextTurn();
    }

    // Metodo di offerta per +10
    public void handleBidPlus10() {
        auctionService.placeFixedBid(10);
        nextTurn();
    }

    // Metodo di offerta per +100
    public void handleBidPlus100() {
        auctionService.placeFixedBid(100);
        nextTurn();
    }

    // Metodo per passare il turno
    public void handlePassTurn() {
        auctionService.pass();
        nextTurn();
    }

    public void closeAuctionWindow(){
        Platform.runLater(() -> {
            if (auctionWindow != null && auctionWindow.getScene() != null) {
                Stage stage = (Stage) auctionWindow.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("La finestra non è stata trovata.");
            }
        });
    }


}
