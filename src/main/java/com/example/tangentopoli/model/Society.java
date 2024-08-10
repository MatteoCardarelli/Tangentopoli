package com.example.tangentopoli.model;

public class Society extends PurchasableBox{
    private int quantity;

    public Society(String name, int price, int mortgageValue) {
        super(name, price, mortgageValue);
    }

    @Override
    public int calculatePrice() {
        if(getOwner().countOwnedSocieties() == 2)
            return getOwner().getLaunchValue() * 10;
        else
            return getOwner().getLaunchValue() * 4;
    }
}
