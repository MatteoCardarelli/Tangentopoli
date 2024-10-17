package com.example.tangentopoli.model;

import com.example.tangentopoli.service.GameService;
import com.example.tangentopoli.service.PlayerService;

public class CommunityChest extends Card {

    public CommunityChest() {
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
