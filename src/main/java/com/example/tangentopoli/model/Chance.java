package com.example.tangentopoli.model;

import com.example.tangentopoli.service.GameService;

public class Chance extends Card {

    public Chance(){
        super();
    };

    @Override
    public void action(GameService game)  {
        Player player = game.getPlayerInTurn();
        game.getController().updateBoxInfo(this.toString());
        System.out.println("cacca pupu");
        switch (this.getType()){
            case 1://paghi
                player.pay(this.getMoneyAmount());
                game.getController().displayPlayerInfo(player);
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
