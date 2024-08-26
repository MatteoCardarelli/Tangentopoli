package com.example.tangentopoli.engine;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.model.Game;

public class GameEngine {
     private static GameEngine instance; // Istanza Singleton

     // Costruttore privato per evitare l'istanza diretta
     public GameEngine(int playersN, BoardController boardController) {
          Game game = new Game(playersN);
          boardController.setGame(game);
          game.setController(boardController);
     }

     // Metodo pubblico statico per ottenere l'istanza Singleton
     public static synchronized GameEngine getInstance(int playersN, BoardController boardController) {
          if (instance == null) {
               instance = new GameEngine(playersN, boardController);
          }
          return instance;
     }

}
