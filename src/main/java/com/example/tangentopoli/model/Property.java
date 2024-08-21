package com.example.tangentopoli.model;

import com.example.tangentopoli.exception.MaximumConstructionOnProperty;

public class Property extends PurchasableBox {
    private int houseN = 0;
    private int hotelN = 0;
    private int housePrice;
    private int hotelPrice;
    private int rentPrice;
    private String color;


    public Property(String name, int price, int rentPrice, int mortgageValue, String color) {
        super(name, price, mortgageValue);
        this.rentPrice = rentPrice;
        this.color = color;
        this.housePrice = price/5;
        this.hotelPrice = price/2;
    }

    public String getColor() {
        return color;
    }

    @Override
    public int calculatePrice() {
        return rentPrice + (housePrice * houseN) + (hotelPrice * 3);
    }

    public int addConstruction() throws MaximumConstructionOnProperty {
        if(houseN < 3 && hotelN == 0) {
            houseN++;
            return housePrice;
        } else if(houseN == 3 && hotelN == 0) {
            hotelN = 1;
            houseN = 0;
            return hotelPrice;
        } else {
            throw new MaximumConstructionOnProperty();
        }
    }
}
