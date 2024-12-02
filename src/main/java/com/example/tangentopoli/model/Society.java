package com.example.tangentopoli.model;

import com.example.tangentopoli.service.PlayerService;

import java.io.Serializable;

public class Society extends PurchasableBox implements Serializable {


    public Society(String name, int price, int mortgageValue) {
        super(name, price, mortgageValue);
    }

    @Override
    public int calculatePrice(PlayerService playerService) {
        int multiplier = playerService.countOwnedSocieties() == 2 ? 10 : 4;
        return multiplier * getOwner().getLaunchValue();
    }
}
