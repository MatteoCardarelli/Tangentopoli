package com.example.tangentopoli.controller;

import com.example.tangentopoli.engine.GameEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button ButtonLoadGame;

    @FXML
    private Button ButtonStartGame;

    @FXML
    private CheckBox CPUCheck;

    @FXML
    private Slider selectPlayerN;

    public void startNewGame(ActionEvent event) {
        int playersN = (int) selectPlayerN.getValue();
        boolean CPUPlayers = CPUCheck.isSelected();
        GameEngine.startNewGame(playersN, CPUPlayers, event);
    }

    public void loadSavedGames(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SavedGames.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Carica partita");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}