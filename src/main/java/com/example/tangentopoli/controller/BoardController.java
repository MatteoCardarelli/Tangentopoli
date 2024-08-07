package com.example.tangentopoli.controller;

import com.example.tangentopoli.TangentopoliApp;
import com.example.tangentopoli.model.Dice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

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

    public void launchDices(ActionEvent actionEvent) {
        int n = Dice.launch();

    }

    public void movePlayer(int n) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
