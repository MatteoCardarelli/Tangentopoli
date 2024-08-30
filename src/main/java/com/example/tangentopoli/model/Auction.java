package com.example.tangentopoli.model;

public class Auction {
    private int cashout;
    private int bid;
    private int winner;


    public Auction(int cashout, int bid, int winner) {
        this.cashout = cashout;
        this.bid = bid;
        this.winner = winner;

    }

    public int getCashout() {
        return cashout;
    }

    public void setCashout(int cashout) {
        this.cashout = cashout;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {this.bid=bid;}

    public int getWinner() {return winner;}

    public void setWinner(int winner) {this.winner = winner;}



}
