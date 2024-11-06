package com.example.tangentopoli.service;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.exception.BoxAlreadyPurchasedException;
import com.example.tangentopoli.exception.MaximumConstructionOnProperty;
import com.example.tangentopoli.exception.NotEnoughMoneyException;
import com.example.tangentopoli.exception.NotYourPropertyException;
import com.example.tangentopoli.model.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class GameService implements Serializable {
    private ObjectMapper objectMapper;
    private Game game;
    BoardController boardController;
    private Map<Integer, PlayerService> playerServiceMap = new HashMap<>();

    public GameService(int playersN, boolean CPUPlayers){
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.game = new Game();
        game.setBoard(new Board(playersN));

        createPlayers(playersN, CPUPlayers);
        loadCards("src/main/resources/serialization/cards/chances.json", "src/main/resources/serialization/cards/communityChests.json");
    }

    public void loadCards(String chanceJsonPath, String communityChestJsonPath) {
        try {
            // Carica e deserializza le carte Chance
            File chanceFile = new File(chanceJsonPath);
            ArrayList<ChanceService> chanceServiceCards = objectMapper.readValue(chanceFile, new TypeReference<ArrayList<ChanceService>>() {});
            Collections.shuffle(chanceServiceCards);
            game.setChanceCards(chanceServiceCards);

            // Carica e deserializza le carte CommunityChest
            File communityChestFile = new File(communityChestJsonPath);
            ArrayList<CommunityChestService> communityChestServiceCards = objectMapper.readValue(communityChestFile, new TypeReference<ArrayList<CommunityChestService>>() {});
            Collections.shuffle(communityChestServiceCards);
            game.setCommunityChestCards(communityChestServiceCards);

            // Ora hai due liste separate di carte
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Populates players list, assigning to each player an ID and the initial amount of money
    private void createPlayers(int playersN, boolean CPUPlayers) {
        game.setPlayers(new ArrayList<>(playersN));
        if (!CPUPlayers) {
            for (int i = 0; i < playersN; i++) {
                HumanPlayer player = new HumanPlayer(i+1, 1000);
                game.getPlayers().add(player);
                playerServiceMap.put(player.getPlayerID(), new PlayerService(player));
                player.setPosition(game.getBoard().getBoxFromIndex(0));   //Set player to start position
            }
        }
        else {
            for (int i = 0; i < playersN; i++) {
                if (i==0) {
                    HumanPlayer player = new HumanPlayer(i + 1, 1000);
                    game.getPlayers().add(player);
                    playerServiceMap.put(player.getPlayerID(), new PlayerService(player));
                    player.setPosition(game.getBoard().getBoxFromIndex(0));   //Set player to start position
                }
                else {
                    CPUPlayer player = new CPUPlayer(i + 1, 1000, this);
                    game.getPlayers().add(player);
                    //Crea CPUPlayerService
                    playerServiceMap.put(player.getPlayerID(), new CPUPlayerService(player));
                    player.setPosition(game.getBoard().getBoxFromIndex(0)); //Set player to start position
                }
            }
        }
    }

    public PlayerService getPlayerService(int playerID) {
        return playerServiceMap.get(playerID);
    }

    public int getPlayersN() {
        return game.getPlayers().size();
    }

    /*public Board getBoard() {
        return board;
    }*/

    public BoardController getController() {
        return boardController;
    }

    public void changeTurn() {
        game.setPlayerInTurn((game.getPlayerInTurnIndex() + 1) % game.getPlayers().size());
        game.setTurn(game.getTurn() + 1);
        Player playerInTurn = game.getPlayerInTurn();
        PlayerService playerService = playerServiceMap.get(playerInTurn.getPlayerID());

        // Controllo per il salto del turno
        if (playerInTurn.isSkipTurn()) {
            playerInTurn.setSkipTurn(false);
            changeTurn(); // Ricorsione per cambiare turno se il giocatore deve saltare
            return;
        }

        // Verifica se il giocatore corrente è un bot
        if (playerInTurn instanceof CPUPlayer && playerService instanceof CPUPlayerService) {
            ((CPUPlayerService) playerService).moveBot();  // Muovi il bot
        }
    }


    /*public int getTurn(){
        return turn;
    }*/

    public Player getPlayerInTurn() {
        return game.getPlayerInTurn();
    }

    //Restituisce la posizione del giocatore im turno (all'interno dell'ArrayList di giocatori)
    public int getPlayerInTurnIndex() { return game.getPlayerInTurnIndex(); }

    //sostituire il valore con player in turn
    public void goToPrison(Player player) {
        player.setInPrison(true);
        player.setPosition(game.getBoard().getBoxes().get((game.getBoard().getSize() / 4)));
        System.out.println("Stampo posizione corrente da Game\n" + player.getPosition().getName());
    }

    public void movePlayer(int diceValue)  {
        Player player = getPlayerInTurn();

        //il giocatore non può muoversi
        if (player.isInPrison() || player.isSkipTurn()) return;

        Box currentPosition = player.getPosition();
        int currentPosIndex = game.getBoard().getBoxIndex(currentPosition);
        int nextPosIndex = currentPosIndex + diceValue;
        Box nextPos = game.getBoard().getBoxFromIndex(nextPosIndex);
        int nextPosIndexMod = game.getBoard().getBoxIndex(nextPos);

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

    // Metodo per iniziare il turno
    public void startTurn() {
        game.setDiceEqualsCombo(0);
        game.setExitPrisonFlag(false);
        PlayerService currentPlayer = getPlayerService(getPlayerInTurn().getPlayerID());

        if (currentPlayer.isInPrison()) {
            if (currentPlayer.getTurnInPrison() == 3) {
                forcePayPrisonFine(currentPlayer);
            }
        }

        // Aggiorna lo stato dei bottoni basato sul giocatore
        boardController.updateButtons(currentPlayer);
    }

    // Logica di quando i dadi hanno lo stesso valore
    public void handleDiceEquals(int diceValue1, int diceValue2) {
        Player currentPlayer = getPlayerInTurn();

        if (game.isExitPrisonFlag()) {
            // Caso in cui il giocatore è uscito di prigione
            boardController.updateButtonsAfterExitPrison();
        } else if (diceValue1 == diceValue2 && !currentPlayer.isInPrison()) {
            game.setDiceEqualsCombo(game.getDiceEqualsCombo() + 1);
            if (currentPlayer instanceof CPUPlayer) {
                ((CPUPlayer) currentPlayer).setCanLaunchAgain(true);  // CPU può tirare di nuovo
            }

            if (game.getDiceEqualsCombo() == 3) {
                sendToPrison(currentPlayer);  // Manda il giocatore in prigione
            } else {
                boardController.updateButtonsForReRoll();  // Permetti un nuovo lancio
            }
        } else {
            game.setDiceEqualsCombo(0);
            boardController.updateButtonsAfterMove();  // Aggiorna i bottoni dopo il movimento
        }
    }

    private void sendToPrison(Player player) {
        // Logica per mandare il giocatore in prigione
        goToPrison(player);
        boardController.disableDiceAndNextButtons();
    }

    // Forza il giocatore a pagare per uscire di prigione se è da 3 turni
    private void forcePayPrisonFine(PlayerService player) {
        // Logica per obbligare il giocatore a pagare
        player.exitFromPrison();
        // Aggiorna i bottoni di conseguenza
        boardController.disableDiceAndNextButtons();
    }

    private ChanceService getChance() {
        ArrayList<ChanceService> chanceServiceCards = game.getChanceCards();
        ChanceService chanceServiceCard = chanceServiceCards.get(0);
        //Positions the first element at the end of the list and shifts all the previous elements
        chanceServiceCards.add(chanceServiceCards.size()-1, chanceServiceCards.remove(0));
        chanceServiceCard.action(this);
        return chanceServiceCard;
    }

    private CommunityChestService getCommunityChest()  {
        ArrayList<CommunityChestService> communityChestServiceCards = game.getCommunityChestCards();
        CommunityChestService communityChestService = communityChestServiceCards.get(0);
        //Positions the first element at the end of the list and shifts all the previous elements
        communityChestServiceCards.add(communityChestServiceCards.size()-1, communityChestServiceCards.remove(0));
        communityChestService.action(this);
        return communityChestService;
    }

    public ArrayList<Integer> getBuildableProperties() {
        ArrayList<Integer> buildablePropertiesIndex = new ArrayList<>();
        Player playerInTurn = getPlayerInTurn();

        // Contiamo quante proprietà ci sono per ogni colore sulla board
        HashMap<String, Integer> colorCountMap = new HashMap<>();
        for (Box box : game.getBoard().getBoxes()) {
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
        for (Box box : game.getBoard().getBoxes()) {
            if (box instanceof Property property) {
                String color = property.getColor();
                int totalColorProperties = colorCountMap.getOrDefault(color, 0);
                int playerColorProperties = playerColorCountMap.getOrDefault(color, 0);

                // Se il player possiede tutte le proprietà di quel colore, aggiungiamo la posizione della proprietà all'array
                if (playerColorProperties == totalColorProperties && !property.isMortgaged()) {
                    int boxIndex = game.getBoard().getBoxIndex(box);
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
        for (int i = 0; i < game.getBoard().getSize(); i++) {
            Box box = game.getBoard().getBoxFromIndex(i);

            // Verifica se la casella è una proprietà
            if (box instanceof Property property) {
                // Verifica se il giocatore è il proprietario
                if (playerInTurn.getPurchasedBoxes().contains(property)) {
                    String color = property.getColor();

                    // Verifica se altre proprietà dello stesso colore hanno costruzioni
                    boolean hasBuildingsInColorSet = false;
                    for (Box otherBox : game.getBoard().getBoxes()) {
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
        PlayerService player = getPlayerService(getPlayerInTurn().getPlayerID());
        player.buildOnProperty((Property) game.getBoard().getBoxFromIndex(pos));
    }

    public void mortgageBox(int pos) {
        PlayerService player = getPlayerService(getPlayerInTurn().getPlayerID());
        player.mortgageBox((PurchasableBox) game.getBoard().getBoxFromIndex(pos));
    }

    public ArrayList<Integer> getExtinguishableBoxes() {
        ArrayList<Integer> extinguishablePropertiesIndex = new ArrayList<>();
        Player playerInTurn = getPlayerInTurn();
        PlayerService player = getPlayerService(playerInTurn.getPlayerID());

        // Itera su tutte le caselle della board
        for (int i = 0; i < game.getBoard().getSize(); i++) {
            Box box = game.getBoard().getBoxFromIndex(i);

            // Verifica se la casella è un'istanza di PurchasableBox
            if (box instanceof PurchasableBox) {
                PurchasableBox purchasableBox = (PurchasableBox) box;

                // Verifica se il giocatore di turno è il proprietario
                if (purchasableBox.getOwner() == playerInTurn) {

                    // Verifica se la proprietà è ipotecata e può essere riscattata
                    if (player.canExtinguishMortgage(purchasableBox)) {
                        extinguishablePropertiesIndex.add(i);
                    }
                }
            }
        }

        return extinguishablePropertiesIndex;
    }

    public void extinguishMortgageBox(int pos) {
        PlayerService player = getPlayerService(getPlayerInTurn().getPlayerID());
        player.extinguishMortgageBox((PurchasableBox) game.getBoard().getBoxFromIndex(pos));
    }

    public ArrayList<Integer> getDemolishProperties() {
        PlayerService player = playerServiceMap.get(game.getPlayerInTurn().getPlayerID());
        ArrayList<Integer> demolishablePropertiesIndex = new ArrayList<>();
        Player playerInTurn = getPlayerInTurn();
        for (int i = 0; i < game.getBoard().getSize(); i++) {
            Box box = game.getBoard().getBoxFromIndex(i);
            if (box instanceof Property) {
                Property property = (Property) box;
                if (property.getOwner() == playerInTurn) {
                    if (player.canDemolish(property)) {
                        demolishablePropertiesIndex.add(i);
                    }
                }
            }
        }
        return demolishablePropertiesIndex;
    }

    public void demolishOnProperty(int pos) {
        PlayerService player = playerServiceMap.get(game.getPlayerInTurn().getPlayerID());
        player.demolishOnProperty((Property) game.getBoard().getBoxFromIndex(pos));
    }

    public void loseGame(Player p) {
        PlayerService player = playerServiceMap.get(p.getPlayerID());
        player.hasLost();
        game.getPlayers().remove(player);
        System.out.println("Loser id: " + player.getPlayerID()+"dall Array");
        if(game.getPlayers().size() < 2) {
            boardController.loseGame(p);
            boardController.gameEnd();
        } else {
            boardController.loseGame(p);
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
                    int boxIndex = game.getBoard().getBoxIndex(box);
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

    /*public ArrayList<Player> getPlayers () {
        return players;
    }*/

    public void startAuctionLogic(Player player) {
        if(player.getPosition() instanceof PurchasableBox && ((PurchasableBox) player.getPosition()).isPurchased()) {
        }
    }

    public void exitFromPrisonWithDicesEqual(int diceValue1, int diceValue2) {
        if (diceValue1==diceValue2 && getPlayerInTurn().isInPrison()){
            getPlayerInTurn().setInPrison(false);

            //disabilita il lancio anche se i valori sono uguali
            boardController.setDiceBtn(true);
            boardController.setNextBtn(false);

            game.setExitPrisonFlag(true);
        }
    }

    public void handlePlayerTurnAfterRol (int diceValue1, int diceValue2){
        // Se il giocatore è in prigione, disabilita il lancio di nuovo anche con valori uguali
        if (getPlayerInTurn().isInPrison()) {
            boardController.setDiceBtn(true);
            boardController.setNextBtn(false);
        } else {
            // Se il valore dei dadi è lo stesso, verifica se il giocatore può rilanciare
            handleDiceEquals(diceValue1, diceValue2);
        }
    }

    public void buyPurchasableBox(PurchasableBox purchasableBox) throws BoxAlreadyPurchasedException, NotEnoughMoneyException {
        // Ottieni il giocatore corrente
        Player currentPlayer = getPlayerInTurn();
        PlayerService player = playerServiceMap.get(currentPlayer.getPlayerID());
        player.buyPurchasableBox(purchasableBox);
    }

    public void setController(BoardController boardController) {
        this.boardController = boardController;
    }

    public Board getBoard() {
        return game.getBoard();
    }

    public ArrayList<Player> getPlayers() {
        return game.getPlayers();
    }

    public void playerAfterMove(){
        Player playerInTurn = game.getPlayerInTurn();
        PlayerService player = playerServiceMap.get(playerInTurn.getPlayerID());
        //metodo per pagare gli affitti di qualsiasi purchasablebox
        if (player.getPosition() instanceof PurchasableBox){
            player.payRent(boardController);
            checkPlayerAfterPayment(game.getPlayerInTurn());
        }
        else if (game.getPlayerInTurn().getPosition() instanceof Tax){
            player.payTax(boardController);
            checkPlayerAfterPayment(game.getPlayerInTurn());
        }

        if (game.getPlayerInTurn() instanceof CPUPlayer && player instanceof CPUPlayerService){

            ((CPUPlayerService) player).takeStrategicDecisions();
        }
    }

    public void exitFromPrison() {
        Player playerInTurn = game.getPlayerInTurn();
        PlayerService player = playerServiceMap.get(playerInTurn.getPlayerID());

        //paga €50 per uscire ed esci dalla prigione
        player.exitFromPrison();
        checkPlayerAfterPayment(playerInTurn);

    }
}
