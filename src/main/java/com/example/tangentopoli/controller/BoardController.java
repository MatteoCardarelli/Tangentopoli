package com.example.tangentopoli.controller;

import com.example.tangentopoli.TangentopoliApp;
import com.example.tangentopoli.model.Dice;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
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


    public void launchDices(ActionEvent actionEvent) {

        int n = Dice.launch();
        int k = Dice.launch();

        // Crea l'animazione di rotazione per il primo dado
        RotateTransition rotateTransition1 = new RotateTransition(Duration.seconds(1), dice1ImageView);
        rotateTransition1.setByAngle(720);
        rotateTransition1.setCycleCount(1);
        rotateTransition1.setOnFinished(event ->
                dice1ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice" + n + ".png")))
        );

        // Crea l'animazione di rotazione per il secondo dado
        RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(1), dice2ImageView);
        rotateTransition2.setByAngle(720);
        rotateTransition2.setCycleCount(1);
        rotateTransition2.setOnFinished(event ->
                dice2ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice" + k + ".png")))
        );

        // Avvia le animazioni
        rotateTransition1.play();
        rotateTransition2.play();
    }

    public void movePlayer(int n) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dice1ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png")));
        dice2ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png")));
    }
}
