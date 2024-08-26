package com.example.tangentopoli.model;

public interface BoxFactory {
    Property createProperty(String name, int price, int rentPrice, int mortgageValue, String color);
    Station createStation(String name, int price, int transitPrice, int mortgageValue);
    Society createSociety(String name, int price, int mortgageValue);
    Prison createPrison();
    Parking createParking();
    Tax createTax(String name, int price);
    Start createStart();
    GoToPrison createGoToPrison();
    CardBox createCardBox(String name);
}
