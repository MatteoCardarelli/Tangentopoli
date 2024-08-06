package com.example.tangentopoli.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

public class MenuController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ButtonLoadGame;

    @FXML
    private Button ButtonStartGame;

    @FXML
    private CheckBox CPUCheckBox;

    @FXML
    private Slider SliderPlayer;

    //function to find the number of players from the slider
    public double getNumPlayers () {
       double NumPlayers = SliderPlayer.getValue();
       return NumPlayers;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}


