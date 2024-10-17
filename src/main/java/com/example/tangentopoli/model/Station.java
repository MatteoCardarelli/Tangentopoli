package com.example.tangentopoli.model;

import com.example.tangentopoli.service.PlayerService;

public class Station extends PurchasableBox {
    private int transitPrice;

    public Station(String name, int price, int transitPrice, int mortgageValue) {
        super(name, price, mortgageValue);
        this.transitPrice = transitPrice;
    }

    @Override
    public int calculatePrice(PlayerService playerService) {
        int multiplier = playerService.countOwnedStations();
        return transitPrice * multiplier;
    }
}
