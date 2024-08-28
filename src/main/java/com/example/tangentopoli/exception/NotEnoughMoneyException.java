package com.example.tangentopoli.exception;

public class NotEnoughMoneyException extends Exception{
    public NotEnoughMoneyException(String nonHaiAbbastanzaSoldi){
        super("Non hai abbastanza soldi!");
    }
}
