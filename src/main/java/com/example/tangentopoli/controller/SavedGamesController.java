package com.example.tangentopoli.controller;

import com.example.tangentopoli.engine.GameEngine;
import com.example.tangentopoli.serialization.GameSaveManager;
import com.example.tangentopoli.serialization.Serializer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SavedGamesController {

    @FXML
    private ListView<String> savedGamesList;

    private final GameSaveManager gameSaveManager = new GameSaveManager();

    @FXML
    public void initialize() {
        loadSavedGames();
    }

    private void loadSavedGames() {
        List<String> savedGames = gameSaveManager.getSavedGames();

        if (savedGames.isEmpty()) {
            savedGamesList.getItems().add("Nessuna partita salvata.");
        } else {
            savedGamesList.getItems().addAll(savedGames);
        }

        savedGamesList.setOnMouseClicked(event -> {
            String selectedGame = savedGamesList.getSelectionModel().getSelectedItem();
            if (selectedGame != null && !selectedGame.equals("Nessuna partita salvata.")) {
                loadGame(selectedGame, event);
            }
        });
    }

    private void loadGame(String selectedGame, Event event) {
        try {
            GameEngine.loadGame(Serializer.deserialize(selectedGame), event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void backToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuView.fxml")); // Path al menu principale
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Tangentopoli - Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading the menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
