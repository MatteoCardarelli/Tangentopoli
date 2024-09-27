package com.example.tangentopoli.model;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.exception.NotEnoughMoneyException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.example.tangentopoli.exception.MaximumConstructionOnProperty;
import com.example.tangentopoli.exception.NotYourPropertyException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Game {
    private Board board;
    private ArrayList<Player> players;
    private ArrayList<CommunityChest> communityChestCards = new ArrayList<>();
    private ArrayList<Chance> chanceCards = new ArrayList<>();
    private BoardController boardController;
    private int turn = 0;
    private int playerInTurn=0;
    private ObjectMapper objectMapper;


    //FAI SINGLETON
    public Game(int playersN, boolean CPUPlayers){
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        board = new Board(playersN);

        createPlayers(playersN, CPUPlayers);
        loadCards("src/main/resources/serialization/cards/chances.json", "src/main/resources/serialization/cards/communityChests.json");
        System.out.println(communityChestCards);
    }

    public void setController(BoardController boardController) {
        this.boardController = boardController;
    }

    public BoardController getController() {
        return boardController;
    }

    public void loadCards(String chanceJsonPath, String communityChestJsonPath) {
        try {
            // Carica e deserializza le carte Chance
            File chanceFile = new File(chanceJsonPath);
            chanceCards = objectMapper.readValue(chanceFile, new TypeReference<ArrayList<Chance>>() {});
            Collections.shuffle(chanceCards);

            // Carica e deserializza le carte CommunityChest
            File communityChestFile = new File(communityChestJsonPath);
            communityChestCards = objectMapper.readValue(communityChestFile, new TypeReference<ArrayList<CommunityChest>>() {});
            Collections.shuffle(communityChestCards);

            // Ora hai due liste separate di carte
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Populates players list, assigning to each player an ID and the initial amount of money
    private void createPlayers(int playersN, boolean CPUPlayers) {
        players = new ArrayList<>(playersN);
        if (!CPUPlayers) {
            for (int i = 0; i < playersN; i++) {
                HumanPlayer player = new HumanPlayer(i+1, 1000);
                players.add(player);
                player.setPosition(board.getBoxFromIndex(0));   //Set player to start position
            }
        }
        else {
            for (int i = 0; i < playersN; i++) {
                if (i==0) {
                    HumanPlayer player = new HumanPlayer(i + 1, 1000);
                    players.add(player);
                    player.setPosition(board.getBoxFromIndex(0));   //Set player to start position
                }
                else {
                    CPUPlayer player = new CPUPlayer(i + 1, 1000, this);
                    players.add(player);
                    player.setPosition(board.getBoxFromIndex(0)); //Set player to start position
                }
            }
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
        if(getPlayerInTurn().isSkipTurn()) {
            getPlayerInTurn().setSkipTurn(false);
            changeTurn();
        }

        if (getPlayerInTurn() instanceof CPUPlayer){
            ((CPUPlayer) getPlayerInTurn()).moveBot();  // Muovi il bot
        }
    }

    public int getTurn(){
        return turn;
    }

    public Player getPlayerInTurn() {
        return players.get(playerInTurn);
    }

    public int getPlayerTurn() { return playerInTurn; }

    //sostituire il valore con player in turn
    public void goToPrison(Player player) {
        player.setInPrison(true);
        player.setPosition(board.getBoxes().get((board.getSize() / 4)));
        System.out.println("Stampo posizione corrente da Game\n" + player.getPosition().getName());
    }

    public void movePlayer(int diceValue)  {
        Player player = getPlayerInTurn();

        //il giocatore non può muoversi
        if (player.isInPrison() || player.isSkipTurn()) return;

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
        else if(nextPos.getName().equals("Imprevisto")) {
            player.setPosition(nextPos);
            Card card = getCommunityChest();
            boardController.setCardInfo(card);
            checkPlayerAfterPayment(player);
        } else if(nextPos.getName().equals("Probabilità")) {
            player.setPosition(nextPos);
            Card card = getChance();
            boardController.setCardInfo(card);
            checkPlayerAfterPayment(player);
        } else
            player.setPosition(nextPos);
    }

    private Chance getChance() {
        Chance chanceCard = chanceCards.get(0);
        //Positions the first element at the end of the list and shifts all the previous elements
        chanceCards.add(chanceCards.size()-1, chanceCards.remove(0));
        chanceCard.action(this);
        return chanceCard;
    }

    private CommunityChest getCommunityChest()  {
        CommunityChest communityChest = communityChestCards.get(0);
        //Positions the first element at the end of the list and shifts all the previous elements
        communityChestCards.add(communityChestCards.size()-1, communityChestCards.remove(0));
        communityChest.action(this);
        return communityChest;
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
                if (playerColorProperties == totalColorProperties && !property.isMortgaged()) {
                    int boxIndex = board.getBoxIndex(box);
                    buildablePropertiesIndex.add(boxIndex);
                }
            }
        }

        return buildablePropertiesIndex;
    }

    public ArrayList<Integer> getMortgageableBoxes() {
        ArrayList<Integer> mortgageablePropertiesIndex = new ArrayList<>();
        Player playerInTurn = getPlayerInTurn();

        // Itera su tutte le caselle della board
        for (int i = 0; i < board.getSize(); i++) {
            Box box = board.getBoxFromIndex(i);

            // Verifica se la casella è una proprietà
            if (box instanceof Property property) {
                // Verifica se il giocatore è il proprietario
                if (playerInTurn.getPurchasedBoxes().contains(property)) {
                    String color = property.getColor();

                    // Verifica se altre proprietà dello stesso colore hanno costruzioni
                    boolean hasBuildingsInColorSet = false;
                    for (Box otherBox : board.getBoxes()) {
                        if (otherBox instanceof Property otherProperty && otherProperty.getColor().equals(color)) {
                            if (otherProperty.hasBuildings()) {
                                hasBuildingsInColorSet = true;
                                break;
                            }
                        }
                    }

                    // Se non ci sono costruzioni nello stesso set di colore e la proprietà è ipotecabile
                    if (!hasBuildingsInColorSet && property.isMortgagable()) {
                        mortgageablePropertiesIndex.add(i);
                    }
                }
            } else if (box instanceof PurchasableBox purchasableBox) {
                // Verifica se il giocatore è il proprietario
                if (purchasableBox.getOwner() == playerInTurn) {
                    // Verifica se la proprietà è ipotecabile
                    if (purchasableBox.isMortgagable()) {
                        mortgageablePropertiesIndex.add(i);
                    }
                }
            }
        }

        return mortgageablePropertiesIndex;
    }

    public void buildOnProperty(int pos) throws NotYourPropertyException, MaximumConstructionOnProperty, NotEnoughMoneyException {
        getPlayerInTurn().buildOnProperty((Property) board.getBoxFromIndex(pos));
    }

    public void mortgageBox(int pos) {
        getPlayerInTurn().mortgageBox((PurchasableBox) board.getBoxFromIndex(pos));
    }

    public ArrayList<Integer> getExtinguishableBoxes() {
        ArrayList<Integer> extinguishablePropertiesIndex = new ArrayList<>();
        Player playerInTurn = getPlayerInTurn();

        // Itera su tutte le caselle della board
        for (int i = 0; i < board.getSize(); i++) {
            Box box = board.getBoxFromIndex(i);

            // Verifica se la casella è un'istanza di PurchasableBox
            if (box instanceof PurchasableBox) {
                PurchasableBox purchasableBox = (PurchasableBox) box;

                // Verifica se il giocatore di turno è il proprietario
                if (purchasableBox.getOwner() == playerInTurn) {

                    // Verifica se la proprietà è ipotecata e può essere riscattata
                    if (playerInTurn.canExtinguishMortgage(purchasableBox)) {
                        extinguishablePropertiesIndex.add(i);
                    }
                }
            }
        }

        return extinguishablePropertiesIndex;
    }

    public void extinguishMortgageBox(int pos) {
        getPlayerInTurn().extinguishMortgageBox((PurchasableBox) board.getBoxFromIndex(pos));
    }

    public ArrayList<Integer> getDemolishableProperties() {
        ArrayList<Integer> demolishablePropertiesIndex = new ArrayList<>();
        Player playerInTurn = getPlayerInTurn();
        for (int i = 0; i < board.getSize(); i++) {
            Box box = board.getBoxFromIndex(i);
            if (box instanceof Property) {
                Property property = (Property) box;
                if (property.getOwner() == playerInTurn) {
                    if (playerInTurn.canDemolish(property)) {
                        demolishablePropertiesIndex.add(i);
                    }
                }
            }
        }
        return demolishablePropertiesIndex;
    }

    public void demolishOnProperty(int pos) {
        getPlayerInTurn().demolishOnProperty((Property) board.getBoxFromIndex(pos));
    }

    public void loseGame(Player player) {
        player.hasLost();
        players.remove(player);
        System.out.println("Loser id: " + player.getPlayerID()+"dall Array");
        if(players.size() < 2) {
            boardController.loseGame(player);
            boardController.gameEnd();
        } else {
            boardController.loseGame(player);
        }
    }

    public void checkPlayerAfterPayment(Player player) {
        if (player.getMoney() <= 0) {
            handlePlayerLoss(player);
        }

        boardController.displayPlayerInfo(player);
    }

    public ArrayList<Integer> getPropertiesWithBuildings() {
        ArrayList<Integer> propertiesWithBuildings = new ArrayList<>();
        Player playerInTurn = getPlayerInTurn();

        for(PurchasableBox box : playerInTurn.getPurchasedBoxes()) {
            if (box instanceof Property property) {
                if(property.hasBuildings()) {
                    int boxIndex = board.getBoxIndex(box);
                    propertiesWithBuildings.add(boxIndex);
                }
            }
        }

        return propertiesWithBuildings;
    }

    public void handlePlayerLoss(Player player) {
        //Controlla che il giocatore abbia costruzioni da demolire e le
        //demolisce finchè non ha abbastanza soldi o sono finite
        ArrayList<Integer> propertiesWithBuildings;
        do {
            propertiesWithBuildings = getPropertiesWithBuildings();
            if (!propertiesWithBuildings.isEmpty()) {
                for (int propertyIndex : propertiesWithBuildings) {
                    int[] coords = boardController.convertPositionToGridCoords(propertyIndex);
                    boardController.demolishOnProperty(coords[0], coords[1]);
                    System.out.println("AAA");
                }
            }
        } while(player.getMoney() <= 0 && !propertiesWithBuildings.isEmpty());


        // Controlla se il giocatore ha proprietà ipotecabili
        ArrayList<Integer> mortgageableProperties = getMortgageableBoxes();
        if (!mortgageableProperties.isEmpty() && player.getMoney() <= 0) {
            for (int propertyIndex : mortgageableProperties) {
                if(player.getMoney() > 0)
                    break;
                int[] coords = boardController.convertPositionToGridCoords(propertyIndex);
                boardController.mortgageBox(coords[0], coords[1]);
            }
        }

        // Verifica se i fondi sono sufficienti per continuare
        if (player.getMoney() <= 0) {
            loseGame(player);
        }
    }

    public ArrayList<Player> getPlayers () {
            return players;
    }

    public void startAuctionLogic(Player player) {
        if(player.getPosition() instanceof PurchasableBox && ((PurchasableBox) player.getPosition()).isPurchased()) {
        }
    }

}