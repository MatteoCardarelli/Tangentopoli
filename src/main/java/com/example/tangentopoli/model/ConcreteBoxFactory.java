package com.example.tangentopoli.model;

public class ConcreteBoxFactory implements BoxFactory {

    @Override
    public Property createProperty(String name, int price, int rentPrice, int mortgageValue, String color) {
        return new Property(name, price, rentPrice, mortgageValue,color );
    }

    @Override
    public Station createStation(String name, int price, int rentPrice, int mortgageValue) {
        return new Station(name, price, rentPrice, mortgageValue);
    }

    @Override
    public Society createSociety(String name, int price, int mortgageValue) {
        return new Society(name, price, mortgageValue);
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
    public Tax createTax(String name, int price) {
        return new Tax(name, price);
    }

    @Override
    public Start createStart() {
        return new Start();
    }

    @Override
    public GoToPrison createGoToPrison() {
        return new GoToPrison();
    }

    @Override
    public CardBox createCardBox(String name) {
        return new CardBox(name);
    }
}
