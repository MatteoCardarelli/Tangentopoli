package com.example.tangentopoli.model;

public class Society extends PurchasableBox{


    public Society(String name, int price, int mortgageValue) {
        super(name, price, mortgageValue);
    }

    @Override
    public int calculatePrice() {
        int multiplier = getOwner().countOwnedSocieties() == 2 ? 10 : 4;
        return multiplier * getOwner().getLaunchValue();
    }
}
