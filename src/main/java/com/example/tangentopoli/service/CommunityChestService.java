package com.example.tangentopoli.service;

import com.example.tangentopoli.model.Card;
import com.example.tangentopoli.model.Player;

public class CommunityChestService extends Card {

    public CommunityChestService() {
        super();

    }

    @Override
    public void action(GameService game) {
        Player p = game.getPlayerInTurn();
        PlayerService player = game.getPlayerService(p.getPlayerID());
        game.getController().updateBoxInfo(this.toString());

        switch (this.getType()){
            case 1://paghi
                player.pay(this.getMoneyAmount());
                game.getController().displayPlayerInfo(p);
                break;
            case 3://salti i turni
                player.setSkipTurn(true);
                break;
            case 6://vai in prigione
                game.getController().sendToPrison();
                break;
        }
    }
}
