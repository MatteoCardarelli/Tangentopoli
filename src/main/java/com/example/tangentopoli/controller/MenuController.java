package com.example.tangentopoli.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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

    //decide quale tabellone aprire e inizializza il GameEngine
    public void startNewGame(ActionEvent event) throws IOException {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Tangentopoli - Play!");
            stage.setScene(scene);
            stage.show();

            new GameEngine((int)selectPlayerN.getValue(), loader.getController(), CPUCheck.isSelected());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadSavedGames(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SavedGames.fxml"));
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


