package com.example.tangentopoli.controller;

import com.example.tangentopoli.TangentopoliApp;
import com.example.tangentopoli.model.Dice;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    private int playerN;
    @FXML
    private Button auctionBtn;

    @FXML
    private TextArea boxInfo;

    @FXML
    private Button buildBtn;

    @FXML
    private Button buyBtn;

    @FXML
    private Button diceBtn;

    @FXML
    private TextArea playerInfo;

    @FXML
    private ImageView dice1ImageView;

    @FXML
    private ImageView dice2ImageView;

    @FXML
    private GridPane boardGrid;

    @FXML
    private List<Circle> playerTokens;


    public void launchDices(ActionEvent actionEvent) {
        Random movement = new Random();
        int x1 = movement.nextInt(200) -100;
        int x2 = movement.nextInt(200) -100;
        int y1 = movement.nextInt(200) -100;
        int y2 = movement.nextInt(200) -100;
        int n = Dice.launch();
        int k = Dice.launch();
        // Crea l'animazione di rotazione per il primo dado
        RotateTransition rotateTransition1 = new RotateTransition(Duration.seconds(1), dice1ImageView);
        rotateTransition1.setByAngle(720);
        rotateTransition1.setCycleCount(1);

        // Crea l'animazione di traslazione per il primo dado
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.3), dice1ImageView);
        translateTransition1.setFromX(0);
        translateTransition1.setFromY(-50);
        translateTransition1.setDelay(Duration.millis(200));
        translateTransition1.setByX(x1); // Imposta la distanza X di traslazione
        translateTransition1.setByY(y1); // Imposta la distanza Y di traslazione

        // Combina le animazioni per il primo dado
        SequentialTransition sequentialTransition1 = new SequentialTransition(translateTransition1 ,rotateTransition1);
        sequentialTransition1.setOnFinished(event ->
                dice1ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice" + n + ".png")))
        );

        // Crea l'animazione di rotazione per il secondo dado
        RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(1), dice2ImageView);
        rotateTransition2.setByAngle(720);
        rotateTransition2.setCycleCount(1);


        // Crea l'animazione di traslazione per il secondo dado
        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.3), dice2ImageView);
        translateTransition2.setFromX(-10);
        translateTransition2.setFromY(50);
        translateTransition2.setDelay(Duration.millis(199));
        translateTransition2.setByX(x2); // Imposta la distanza X di traslazione
        translateTransition2.setByY(y2); // Imposta la distanza Y di traslazione


        // Combina le animazioni per il secondo dado
        SequentialTransition sequentialTransition2 = new SequentialTransition( translateTransition2 ,rotateTransition2);
        sequentialTransition2.setOnFinished(event ->
                dice2ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice" + k + ".png")))
        );

        // Avvia le animazioni
        sequentialTransition1.play();
        sequentialTransition2.play();

    }

    public void setPlayerN(int playerN) {
        this.playerN = playerN;
        createPlayerTokens();
    }

    public void setPlayerTokenPosition(int playerIndex, int row, int column) {
        Circle playerToken = playerTokens.get(playerIndex);
        GridPane.setRowIndex(playerToken, row);
        GridPane.setColumnIndex(playerToken, column);
        switch(playerIndex){
            case 0:playerToken.setTranslateY(-47.0);
                   playerToken.setTranslateX(5.0);
            break;
            case 1:playerToken.setTranslateX(47.0);
            break;
            case 2:playerToken.setTranslateX(87.0);
                   playerToken.setTranslateY(-47.0);
                   break;
        }
    }

    public void createPlayerTokens() {
        playerTokens = new ArrayList<>();
        for (int i = 0; i < playerN; i++) {
            Circle playerToken = new Circle(20, Color.RED); // Cerchio rosso con raggio 10
            playerTokens.add(playerToken);
            boardGrid.add(playerToken, 0, 0); // Posiziona le pedine inizialmente nella cella centrale
            setPlayerTokenPosition(i, 0, 0); // Sposta il giocatore alla cella iniziale
        }
    }

    public void movePlayer(int n) {
        // Logica per muovere il giocatore di 'n' spazi
        // Aggiorna l'interfaccia utente o altri elementi secondo necessità
        playerInfo.appendText("Player moves " + n + " spaces.\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dice1ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png")));
        dice2ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png")));

        // Assicurati che la lista di pedine sia inizializzata
        playerTokens = new ArrayList<>();
    }
}