package com.example.tangentopoli.engine;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.model.Game;
import com.example.tangentopoli.service.GameService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameEngine {

     /**
      * Starts a new game and initializes the game board.
      *
      * @param playersN   Number of players.
      * @param CPUPlayers Whether there are CPU players.
      * @param event      The ActionEvent to determine the current window.
      */
     public static void startNewGame(int playersN, boolean CPUPlayers, ActionEvent event) {
          try {
               // Determine the appropriate board based on the number of players
               String path = "";
               switch (playersN) {
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
                         throw new IllegalArgumentException("Invalid number of players: " + playersN);
               }

               // Load the board
               FXMLLoader loader = new FXMLLoader(GameEngine.class.getResource(path));
               Parent root = loader.load();
               Scene scene = new Scene(root);
               Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
               stage.setTitle("Tangentopoli - Play!");
               stage.setScene(scene);
               stage.show();

               // Initialize the game and set up the controller
               BoardController boardController = loader.getController();
               newGame(playersN, boardController, CPUPlayers);

          } catch (Exception e) {
               System.err.println("Error starting new game: " + e.getMessage());
               e.printStackTrace();
          }
     }

     /**
      * Method to initialize a new game.
      *
      * @param playersN        Number of players.
      * @param boardController The board controller.
      * @param CPUPlayers      Indicates if there are CPU players.
      */
     public static void newGame(int playersN, BoardController boardController, boolean CPUPlayers) {
          GameService game = new GameService(playersN, CPUPlayers);
          boardController.setGame(game);
          game.setController(boardController);
     }

     /**
      * Loads a saved game and initializes it with the deserialized data.
      *
      * @param deserializedGame The deserialized game object.
      * @param event            The ActionEvent to get the current window.
      */
     public static void loadGame(Object deserializedGame, Event event) {
          try {
               GameService game = (GameService) deserializedGame;
               String path = "";

               int playersN = game.getPlayersN();
               switch (playersN) {
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
                         throw new IllegalArgumentException("Invalid number of players: " + playersN);
               }

               FXMLLoader loader = new FXMLLoader(GameEngine.class.getResource(path));
               Parent root = loader.load();
               Scene scene = new Scene(root);
               Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
               stage.setTitle("Tangentopoli - Play!");
               stage.setScene(scene);
               stage.show();

               BoardController boardController = loader.getController();
               boardController.setLoadedGame(game);
               game.setController(boardController);

          } catch (Exception e) {
               System.err.println("Error loading game: " + e.getMessage());
               e.printStackTrace();
          }
     }
}