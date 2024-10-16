package com.example.tangentopoli.engine;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.model.Game;
import com.example.tangentopoli.service.GameService;

public class GameEngine {
     private static GameEngine instance; // Istanza Singleton

     // Costruttore privato per evitare l'istanza diretta
     public GameEngine(int playersN, BoardController boardController, boolean CPUPlayers) {
          GameService game = new GameService(playersN, CPUPlayers );
          boardController.setGame(game);
          game.setController(boardController);
     }

     // Metodo pubblico statico per ottenere l'istanza Singleton
     public static synchronized GameEngine getInstance(int playersN, BoardController boardController, boolean CPUPlayers) {
          if (instance == null) {
               instance = new GameEngine(playersN, boardController, CPUPlayers);
          }
          return instance;
     }

}
