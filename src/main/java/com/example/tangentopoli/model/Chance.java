package com.example.tangentopoli.model;

public class Chance extends Card {
    public Chance(String title, String description, int type, int moneyAmount) {
        super(title, description, type, moneyAmount);
    }

    public Chance(){
        super();
    };

    @Override
    public void action(Game game) {
        Player player = game.getPlayerInTurn();
        game.getController().updateBoxInfo(this.toString());
        System.out.println("cacca pupu");
        switch (this.getType()){
            case 1://paghi
                player.setMoney(player.getMoney() + this.getMoneyAmount());
                game.getController().displayPlayerInfo(player);

                break;
            case 2://vieni pagato
                player.setMoney(player.getMoney() + this.getMoneyAmount());
                game.getController().displayPlayerInfo(player);
                break;
            case 3://salti i turni
                player.setSkipTurn(true);
                break;
            case 4://ti sposti avanti di tre caselle
                game.getController().movePlayer(3);
                break;
            case 5://esci di prigione
                player.setExitFromPrisonCard(true);
                break;
            case 6://vai in prigione
                game.getController().sendToPrison();
                break;
        }
    }
}
