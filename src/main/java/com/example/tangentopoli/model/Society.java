package com.example.tangentopoli.model;

public class Society extends PurchasableBox{
    private int quantity;

    public Society(String name, int price, int transitPrice, int mortgageValue) {
        super(name, price, mortgageValue);
    }

    public int calculatePrice(int diceValue) {
        int rentPrice;
        if (quantity == 1) {
            return rentPrice = diceValue *4;
        }

        return rentPrice = diceValue * 10;
    }

    @Override
    public int calculatePrice() {
        return 0;
    }
}
