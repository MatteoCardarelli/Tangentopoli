package com.example.tangentopoli.service;

import com.example.tangentopoli.controller.AuctionController;
import com.example.tangentopoli.model.Auction;
import com.example.tangentopoli.model.Player;
import com.example.tangentopoli.model.PurchasableBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionService {
    private ArrayList<PlayerService> playersInAuction;
    private int currentBid;
    private PlayerService highestBidder;
    private int currentTurnIndex;
    private PurchasableBox propertyOnAuction;
    private AuctionController auctionController;
    int currentPlayerIndex=0;

    public AuctionService(GameService gameService, AuctionController auctionController) {
        this.playersInAuction = new ArrayList<>(gameService.getPlayerServiceMap().values());
         // Copia della lista originale
        this.currentBid = 10;
        this.highestBidder = null;
        this.currentTurnIndex = (gameService.getPlayerInTurnIndex() + 1) % playersInAuction.size();
        this.propertyOnAuction = (PurchasableBox) gameService.getPlayerInTurn().getPosition();
        this.auctionController = auctionController;
    }

    public int getCurrentBid() {return currentBid;}

    public PlayerService getCurrentPlayer() {
        /*if (playersInAuction == null || playersInAuction.isEmpty()) {
            throw new IllegalStateException("La lista dei giocatori in asta è vuota.");
        }
        if (currentTurnIndex < 0 || currentTurnIndex >= playersInAuction.size()) {
            throw new IndexOutOfBoundsException(
                    "Indice del giocatore corrente non valido: " + currentTurnIndex +
                            " (dimensione lista: " + playersInAuction.size() + ")"
            );
        }*/
        return playersInAuction.get(currentTurnIndex);
    }



    public void placeFixedBid(int increment) {
        int bidAmount = currentBid + increment;
        placeBid(bidAmount);
    }

    public boolean placeBid(int bidAmount) {
        if (bidAmount > currentBid && bidAmount < getCurrentPlayer().getMoney()) {
            currentBid = bidAmount;
            highestBidder = getCurrentPlayer();
            System.out.println("Offerta più alta Giocatore" + highestBidder.getPlayerID());
            nextTurn(); // Passa al turno successivo
            return true;
        } else {
            System.out.println("Offerta non valida.");
            return false;
        }
    }

    public void pass() {
        if (playersInAuction == null || playersInAuction.isEmpty()) {
            throw new IllegalStateException("Nessun giocatore disponibile per continuare l'asta.");
        }

        playersInAuction.remove(currentTurnIndex);

        // Controlla se rimane un solo giocatore
        if (playersInAuction.size() <= 1) {
            concludeAuction();
            return;
        }

        // Aggiorna l'indice solo se necessario
        if (currentTurnIndex >= playersInAuction.size()) {
            currentTurnIndex = 0; // Reset se l'indice supera la dimensione della lista
        }

        // Aggiorna il turno
        auctionController.nextTurn();
    }


    private void nextTurn() {
        if (playersInAuction == null || playersInAuction.isEmpty()) {
            throw new IllegalStateException("Nessun giocatore rimasto per passare al turno successivo.");
        }
        currentTurnIndex = (currentTurnIndex + 1) % playersInAuction.size();
    }


    private void concludeAuction() {
        if (highestBidder != null) {
            highestBidder.setAsAuctionWinner(propertyOnAuction, currentBid);
            auctionController.updatePurchasedBoxUI(highestBidder);
        } else {
            System.out.println("Nessun vincitore per l'asta.");
        }
        auctionController.closeAuctionWindow();
    }
}
