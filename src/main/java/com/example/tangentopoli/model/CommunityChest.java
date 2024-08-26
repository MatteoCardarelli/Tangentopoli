package com.example.tangentopoli.model;

public class CommunityChest extends Card {
    public CommunityChest(String title, String description, int type, int moneyAmount) {
        super(title, description, type, moneyAmount);
    }

    public CommunityChest() {
        super();

    }

    @Override
    public void action(Player player) {

    }
}
