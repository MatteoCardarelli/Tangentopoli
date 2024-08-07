package com.example.tangentopoli.engine;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.model.Game;

public class GameEngine {
     BoardController boardController;

     public GameEngine(int playersN, BoardController boardController){
          Game game= new Game(playersN);
          this.boardController = boardController;
          boardController.setPlayerN(playersN);
     }
}
