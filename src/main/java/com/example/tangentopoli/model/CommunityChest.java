package com.example.tangentopoli.model;

public class CommunityChest extends Card {

    public CommunityChest() {
        super();

    }

    @Override
    public void action(Game game) {
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
            case 6://vai in prigione
                game.getController().sendToPrison();
                break;
        }
    }
}
