package com.example.tangentopoli.model;
import com.example.tangentopoli.service.GameService;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.Random;

public class CPUPlayer extends Player {

    private GameService game;
    private boolean canLaunchAgain = false;

    public CPUPlayer(int playerID, int money, GameService game) {
        super(playerID, money);
        this.game = game;
    }

    public void setCanLaunchAgain(boolean canLaunchAgain){
        this.canLaunchAgain = canLaunchAgain;
    }

    public boolean isCanLaunchAgain(){
        return canLaunchAgain;
    }

    public GameService getGame () {
        return game;
    }

    public void setGame(GameService game) {
        this.game = game;
    }


}


