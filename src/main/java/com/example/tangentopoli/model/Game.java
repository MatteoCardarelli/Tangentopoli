package com.example.tangentopoli.model;

import com.example.tangentopoli.exception.MaximumConstructionOnProperty;
import com.example.tangentopoli.exception.NotYourPropertyException;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private Board board;
    private ArrayList<Player> players;
    private Deck communityChests;
    private Deck chanches;
    private Dice dice;
    private int turn = 0;
    private int playerInTurn=0;

    //FAI SINGLETON
    public Game(int playersN){
        board = new Board(playersN);
        createPlayers(playersN);
    }

    //Populates players list, assigning to each player an ID and the initial amount of money
    private void createPlayers(int playersN) {
        players = new ArrayList<>(playersN);
        for (int i = 0; i < playersN; i++) {
            HumanPlayer player = new HumanPlayer(i+1, 1500);
            players.add(player);
            player.setPosition(board.getBoxFromIndex(0));   //Set player to start position
        }
    }

    public int getPlayersN() {
        return players.size();
    }

    public Board getBoard() {
        return board;
    }

    public void changeTurn() {
        playerInTurn = (playerInTurn + 1) % players.size();
        turn++;
    }

    public int getTurn(){
        return turn;
    }

    public Player getPlayerInTurn() {
        return players.get(playerInTurn);
    }

    public int getPlayerTurn() { return playerInTurn; }

    public void goToPrison(Player player) {
        player.setInPrison(true);
        player.setPosition(board.getBoxes().get((board.getSize() / 4)));
    }

    public void movePlayer(int diceValue) {
        Player player = getPlayerInTurn();

        //il giocatore non può muoversi
        if (player.isInPrison()) return;

        Box currentPosition = player.getPosition();
        int currentPosIndex = board.getBoxIndex(currentPosition);
        int nextPosIndex = currentPosIndex + diceValue;
        Box nextPos = board.getBoxFromIndex(nextPosIndex);
        int nextPosIndexMod = board.getBoxIndex(nextPos);

        //Once the player passes on the START the game gives him 200€
        if(nextPosIndexMod < currentPosIndex) {
            player.setMoney(player.getMoney() + 200);
        }
        if(nextPos.getName().equals("GoToPrison"))
            goToPrison(player);
        else
            player.setPosition(nextPos);
    }

    public ArrayList<Integer> getBuildableProperties() {
        ArrayList<Integer> buildablePropertiesIndex = new ArrayList<>();
        Player playerInTurn = getPlayerInTurn();

        // Contiamo quante proprietà ci sono per ogni colore sulla board
        HashMap<String, Integer> colorCountMap = new HashMap<>();
        for (Box box : board.getBoxes()) {
            if (box instanceof Property property) {
                String color = property.getColor();
                colorCountMap.put(color, colorCountMap.getOrDefault(color, 0) + 1);
            }
        }

        // Contiamo quante proprietà di ciascun colore possiede il player
        HashMap<String, Integer> playerColorCountMap = new HashMap<>();
        for (PurchasableBox purchasedBox : playerInTurn.getPurchasedBoxes()) {
            if (purchasedBox instanceof Property property) {
                String color = property.getColor();
                playerColorCountMap.put(color, playerColorCountMap.getOrDefault(color, 0) + 1);
            }
        }

        // Verifichiamo se il giocatore possiede tutte le proprietà di un determinato colore
        for (Box box : board.getBoxes()) {
            if (box instanceof Property property) {
                String color = property.getColor();
                int totalColorProperties = colorCountMap.getOrDefault(color, 0);
                int playerColorProperties = playerColorCountMap.getOrDefault(color, 0);

                // Se il player possiede tutte le proprietà di quel colore, aggiungiamo la posizione della proprietà all'array
                if (playerColorProperties == totalColorProperties) {
                    int boxIndex = board.getBoxIndex(box);
                    buildablePropertiesIndex.add(boxIndex);
                }
            }
        }

        return buildablePropertiesIndex;
    }

    public void buildOnProperty(int pos) throws NotYourPropertyException, MaximumConstructionOnProperty {
        getPlayerInTurn().buildOnProperty((Property) board.getBoxFromIndex(pos));
    }
}


