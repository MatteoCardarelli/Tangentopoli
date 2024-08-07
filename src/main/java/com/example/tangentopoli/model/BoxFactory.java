package com.example.tangentopoli.model;

public interface BoxFactory {
    Property createProperty(String name, int price, int rentPrice, int mortgageValue);
    Station createStation(String name, int price, int transitPrice, int mortgageValue);
    Society createSociety(String name, int price, int rentPrice, int mortgageValue);
    Prison createPrison();
    Parking createParking();
    Tax createTax();
    Start createStart();
}
