package com.example.tangentopoli.model;

import com.example.tangentopoli.exception.NotEnoughMoneyException;

public class CommunityChest extends Card {
    public CommunityChest(String title, String description, int type, int moneyAmount) {
        super(title, description, type, moneyAmount);
    }

    public CommunityChest() {
        super();

    }

    @Override
    public void action(Game game) throws NotEnoughMoneyException {
        Player player = game.getPlayerInTurn();
        game.getController().updateBoxInfo(this.toString());

        switch (this.getType()){
            case 1://paghi
                player.pay(this.getMoneyAmount());
                game.getController().displayPlayerInfo(player);
                break;
            case 3://salti i turni
                player.setSkipTurn(true);
                break;
            case 4://ti sposti avanti di tre caselle
                game.getController().movePlayer(3);
                break;
            case 6://vai in prigione
                game.getController().sendToPrison();
                break;
        }
    }
}
