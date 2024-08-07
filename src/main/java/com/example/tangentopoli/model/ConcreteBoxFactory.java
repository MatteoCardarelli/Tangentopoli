package com.example.tangentopoli.model;

public class ConcreteBoxFactory implements BoxFactory {

    @Override
    public Property createProperty(String name, int price, int rentPrice, int mortgageValue) {
        return new Property(name, price, rentPrice, mortgageValue);
    }

    @Override
    public Station createStation(String name, int price, int transitPrice, int mortgageValue) {
        return new Station(name, price, transitPrice, mortgageValue);
    }

    @Override
    public Society createSociety(String name, int price, int rentPrice, int mortgageValue) {
        return new Society(name, price, rentPrice, mortgageValue);
    }

    @Override
    public Prison createPrison() {
        return new Prison();
    }

    @Override
    public Parking createParking() {
        return new Parking();
    }

    @Override
    public Tax createTax() {
        return new Tax();
    }

    @Override
    public Start createStart() {
        return new Start();
    }
}
