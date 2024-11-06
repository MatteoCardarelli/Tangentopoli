package com.example.tangentopoli.service;

import com.example.tangentopoli.model.Card;
import com.example.tangentopoli.model.Player;

public class ChanceService extends Card {

    public ChanceService(){
        super();
    };

    @Override
    public void action(GameService game)  {
        Player p = game.getPlayerInTurn();
        PlayerService player = game.getPlayerService(p.getPlayerID());
        game.getController().updateBoxInfo(this.toString());
        System.out.println("cacca pupu");
        switch (this.getType()){
            case 1://paghi
                player.pay(this.getMoneyAmount());
                game.getController().displayPlayerInfo(p);
                break;
            case 3://salti i turni
                player.setSkipTurn(true);
                break;
            case 5://esci di prigione
                player.setExitFromPrisonCard(true);
                break;
        }
    }
}
