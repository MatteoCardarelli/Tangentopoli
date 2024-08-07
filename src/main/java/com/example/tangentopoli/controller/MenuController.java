package com.example.tangentopoli.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.example.tangentopoli.TangentopoliApp;
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
    private CheckBox CPUCheck;

    @FXML
    private Slider selectPlayerN;

    public void startNewGame(ActionEvent event) throws IOException {
        new GameEngine((int)selectPlayerN.getValue());
        try {
            String path="";
            switch ((int)selectPlayerN.getValue()) {
                case 2, 3:
                    path = "/fxml/SmallBoardGrid.fxml";
                    break;
                case 4, 5:
                    path = "/fxml/MediumBoard.fxml";
                    break;
                case 6:
                    path = "/fxml/LargeBoard.fxml";
                    break;
                default:
                    break;
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Tangentopoli - Play!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}


