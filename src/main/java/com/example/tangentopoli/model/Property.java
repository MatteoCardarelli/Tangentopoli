package com.example.tangentopoli.model;

import com.example.tangentopoli.exception.MaximumConstructionOnProperty;
import com.example.tangentopoli.service.PlayerService;

import java.io.Serializable;

public class Property extends PurchasableBox implements Serializable {
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
    public int calculatePrice(PlayerService playerService) {
        return rentPrice + (housePrice * houseN) + (hotelPrice * hotelN * 3);
    }

    public void addConstruction() throws MaximumConstructionOnProperty {
        if(houseN < 4 && hotelN == 0) {
            houseN++;
        } else if(houseN == 4 && hotelN == 0) {
            hotelN = 1;
            houseN = 0;
        } else {
            throw new MaximumConstructionOnProperty();
        }
    }

    public int getCostructionPrice() throws MaximumConstructionOnProperty {
        if(houseN < 4 && hotelN == 0) {
            return housePrice;
        }
        else if(houseN == 4 && hotelN == 0) {
            return hotelPrice;
        }
        else throw new MaximumConstructionOnProperty();
    }

    public int getHouseN() {
        return houseN;
    }

    public int getHotel() {
        return hotelN;
    }

    public int getDemolitionPrice() {
        if(hotelN == 1) {
            return hotelPrice/2;
        }
        else {
            return housePrice/2;
        }
    }

    public void demolishConstruction() {
        if(hotelN == 1) {
            hotelN = 0;
            houseN = 4;
        } else {
            houseN --;
        }
    }

    public boolean hasBuildings() {
        return(hotelN !=0 || houseN !=0);
    }
}
