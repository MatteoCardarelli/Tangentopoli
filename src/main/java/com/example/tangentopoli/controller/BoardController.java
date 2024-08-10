package com.example.tangentopoli.controller;

import com.example.tangentopoli.exception.BoxAlreadyPurchasedException;
import com.example.tangentopoli.exception.NotEnoughMoneyException;
import com.example.tangentopoli.model.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    private int playerN;

    private Game game;
    @FXML
    private Button nextBtn;

    @FXML
    private TextArea boxInfo;

    @FXML
    private Button buildBtn;

    @FXML
    private Button buyBtn;

    @FXML
    private Button diceBtn;

    @FXML
    private TextArea playerInfo;

    @FXML
    private ImageView dice1ImageView;

    @FXML
    private ImageView dice2ImageView;

    @FXML
    private GridPane boardGrid;

    @FXML
    private List<Circle> playerTokens;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dice1ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png"))); //inizializzazione dadi
        dice2ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png")));

        nextBtn.setDisable(true);
        buyBtn.setDisable(true);
    }


    public void launchDices(ActionEvent actionEvent) {
        Random movement = new Random();
        Player playerInTurn = game.getPlayerInTurn();
        int x1 = movement.nextInt(200) - 100; //numeri random per lanciare i dadi
        int x2 = movement.nextInt(200) - 100;
        int y1 = movement.nextInt(200) - 100;
        int y2 = movement.nextInt(200) - 100;

        int diceValue1 = Dice.launch();
        int diceValue2 = Dice.launch();
        int diceValue = diceValue1 + diceValue2;

        // Crea l'animazione di rotazione per il primo dado
        RotateTransition rotateTransition1 = new RotateTransition(Duration.seconds(1), dice1ImageView);
        rotateTransition1.setByAngle(720);
        rotateTransition1.setCycleCount(1);

        // Crea l'animazione di traslazione per il primo dado
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.3), dice1ImageView);
        translateTransition1.setFromX(0);
        translateTransition1.setFromY(-50);
        translateTransition1.setDelay(Duration.millis(200));
        translateTransition1.setByX(x1); // Imposta la distanza X di traslazione
        translateTransition1.setByY(y1); // Imposta la distanza Y di traslazione

        // Combina le animazioni per il primo dado
        SequentialTransition sequentialTransition1 = new SequentialTransition(translateTransition1, rotateTransition1);
        sequentialTransition1.setOnFinished(event ->
                dice1ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice" + diceValue1 + ".png")))
        );

        // Crea l'animazione di rotazione per il secondo dado
        RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(1), dice2ImageView);
        rotateTransition2.setByAngle(720);
        rotateTransition2.setCycleCount(1);

        // Crea l'animazione di traslazione per il secondo dado
        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.3), dice2ImageView);
        translateTransition2.setFromX(-10);
        translateTransition2.setFromY(50);
        translateTransition2.setDelay(Duration.millis(199));
        translateTransition2.setByX(x2); // Imposta la distanza X di traslazione
        translateTransition2.setByY(y2); // Imposta la distanza Y di traslazione

        // Combina le animazioni per il secondo dado
        SequentialTransition sequentialTransition2 = new SequentialTransition(translateTransition2, rotateTransition2);
        sequentialTransition2.setOnFinished(event ->
                dice2ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice" + diceValue2 + ".png")))
        );

        // Combina le animazioni di entrambi i dadi in un'unica transizione parallela
        ParallelTransition parallelTransition = new ParallelTransition(sequentialTransition1, sequentialTransition2);

        // Aggiungi un listener per eseguire il movimento del giocatore dopo il completamento delle animazioni
        parallelTransition.setOnFinished(event -> {
            movePlayer(diceValue);

            //se il valore dei dadi è lo stesso lancia di nuovo
            if (diceValue1 == diceValue2){
                diceBtn.setDisable(false);  //abilita il bottone
                nextBtn.setDisable(true);//disabilita il bottone
                buyBtn.setDisable(false);
            }
            else {
                diceBtn.setDisable(true);   // Disabilita il bottone dopo aver mosso la pedina
                nextBtn.setDisable(false);  // Abilita il bottono
                buyBtn.setDisable(false);
            }
            try {
                playerInTurn.payRent(this);
            } catch (NotEnoughMoneyException e) {
                throw new RuntimeException(e);
            }
        });

        // Avvia la transizione parallela (che avvierà tutte le animazioni)
        parallelTransition.play();
    }


    //metodo che imposta il numero di pedine
    public void setPlayerN() {
        this.playerN = game.getPlayersN();
        createPlayerTokens();
    }

    public void setGame(Game game) {
        this.game = game;
        setPlayerN();
        displayPlayerInfo(game.getPlayerInTurn());
        displayBoxInfo(game.getPlayerInTurn());
    }

    public void nextTurn() {
        game.changeTurn();
        displayPlayerInfo(game.getPlayerInTurn());
        displayBoxInfo(game.getPlayerInTurn());
        diceBtn.setDisable(false);
        nextBtn.setDisable(true);
        buyBtn.setDisable(true);
    }

    //Displays player data in the top textArea
    public void displayPlayerInfo(Player player) {
        String playerInfoStr = player.toString();
        playerInfo.setText(playerInfoStr);
    }

    public void updateBoxInfo(String message) {
        boxInfo.setText(message);
    }

    //Displays the current player's Box infos
    private void displayBoxInfo(Player player) {
        String boxInfoStr = "Ti trovi su:\n" + player.getPosition().getDescription();
        boxInfo.setText(boxInfoStr);
    }

    //metodo che posiziona le pedine nel tabellone piccolo
    public void setPlayerTokenPositionSmall(int playerIndex, int row, int column) {
        Circle playerToken = playerTokens.get(playerIndex);
        GridPane.setRowIndex(playerToken, row);
        GridPane.setColumnIndex(playerToken, column);
        switch (playerIndex) {
            case 0:
                playerToken.setTranslateY(-47.0);
                playerToken.setTranslateX(5.0);

                break;
            case 1:
                playerToken.setTranslateX(47.0);
                break;
            case 2:
                playerToken.setTranslateX(87.0);
                playerToken.setTranslateY(-47.0);
                break;
            default:
                break;
        }
    }

    //metodo che posiziona le pedine nel tabellone medio
    public void setPlayerTokenPositionMedium(int playerIndex, int row, int column) {
        Circle playerToken = playerTokens.get(playerIndex);
        playerToken.setRadius(16);
        GridPane.setRowIndex(playerToken, row);
        GridPane.setColumnIndex(playerToken, column);
        switch (playerIndex) {
            case 0:
                playerToken.setTranslateY(-42.0);
                playerToken.setTranslateX(2.0);
                break;
            case 1:
                playerToken.setTranslateX(20.0);
                break;
            case 2:
                playerToken.setTranslateX(38.0);
                playerToken.setTranslateY(-42.0);
                break;
            case 3:
                playerToken.setTranslateX(74.0);
                playerToken.setTranslateY(-42.0);
                break;
            case 4:
                playerToken.setTranslateX(56.0);
                break;
            default:
                break;
        }
    }

    public void setPlayerTokenPositionLarge(int playerIndex, int row, int column) {
        Circle playerToken = playerTokens.get(playerIndex);
        playerToken.setRadius(12.8);
        GridPane.setRowIndex(playerToken, row);
        GridPane.setColumnIndex(playerToken, column);
        switch (playerIndex) {
            case 0:
                playerToken.setTranslateY(-32.0);
                playerToken.setTranslateX(2.0);
                break;
            case 1:
                playerToken.setTranslateX(28.0);
                break;
            case 2:
                playerToken.setTranslateX(28.0);
                playerToken.setTranslateY(-32.0);
                break;
            case 3:
                playerToken.setTranslateX(54.0);
                playerToken.setTranslateY(-32.0);
                break;
            case 4:
                playerToken.setTranslateX(54.0);
                break;
            case 5:
                playerToken.setTranslateX(2.0);
                break;
            default:
                break;
        }
    }


    //metodo che crea le pedine
    public void createPlayerTokens() {
        playerTokens = new ArrayList<>();
        // Definisci una lista di colori
        List<Color> playerColors = List.of(
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
                Color.ORANGE, Color.PURPLE // Aggiungi altri colori se necessario
        );

        for (int i = 0; i < playerN; i++) {
            // Usa il colore corrispondente per ogni giocatore
            Color playerColor = playerColors.get(i % playerColors.size());

            Circle playerToken = new Circle(20, playerColor); // Cerchio con raggio 20 e colore specifico
            playerTokens.add(playerToken);
            boardGrid.add(playerToken, 0, 0); // Posiziona le pedine inizialmente nella cella centrale

            switch (playerN) {
                case 2, 3:
                    setPlayerTokenPositionSmall(i, 0, 0); // Sposta il giocatore alla cella iniziale
                    break;
                case 4, 5:
                    setPlayerTokenPositionMedium(i, 0, 0);
                    break;
                case 6:
                    setPlayerTokenPositionLarge(i, 0, 0);
                    break;
                default:
                    break;
            }
        }
    }



    public void movePlayer(int diceValue) {
        Circle playerToken = playerTokens.get(game.getPlayerTurn());
        int rowPos = GridPane.getRowIndex(playerToken) == null ? 0 : GridPane.getRowIndex(playerToken);
        int columnPos = GridPane.getColumnIndex(playerToken) == null ? 0 : GridPane.getColumnIndex(playerToken);
        int maxC = boardGrid.getColumnCount();
        int maxR = boardGrid.getRowCount();

        for (int i = 0; i < diceValue; i++) {
            // Movimento orizzontale da sinistra a destra
            if (rowPos == 0 && columnPos < maxC - 1) {
                columnPos++;
            }
            // Movimento verticale giù nella colonna di destra
            else if (columnPos == maxC - 1 && rowPos < maxR - 1) {
                rowPos++;
            }
            // Movimento orizzontale da destra a sinistra
            else if (rowPos == maxR - 1 && columnPos > 0) {
                columnPos--;
            }
            // Movimento verticale su nella colonna di sinistra
            else if (columnPos == 0 && rowPos > 0) {
                rowPos--;
            }

            // Aggiorna la posizione della pedina
            switch(playerN){
                case 2, 3:
                    setPlayerTokenPositionSmall(game.getPlayerTurn(), rowPos, columnPos);
                    break;
                case 4, 5:
                    setPlayerTokenPositionMedium(game.getPlayerTurn(), rowPos, columnPos);
                    break;
                case 6:
                    setPlayerTokenPositionLarge(game.getPlayerTurn(), rowPos, columnPos);
                    break;
                default:
                    break;
            }
        }
        game.movePlayer(diceValue);
        displayBoxInfo(game.getPlayerInTurn());
    }

    public void buyPress( ) throws NotEnoughMoneyException,BoxAlreadyPurchasedException {
        // Ottieni il giocatore corrente
        Player currentPlayer = game.getPlayerInTurn();

        // Ottieni la casella su cui si trova il giocatore
        Box currentBox = currentPlayer.getPosition();

        // Controlla se la casella corrente è una PurchasableBox
        if (currentBox instanceof PurchasableBox purchasableBox) {
            try {
                // Tenta di acquistare la casella
                currentPlayer.buyPurchasableBox(purchasableBox);

                // Aggiorna l'interfaccia con le nuove informazioni
                displayPlayerInfo(currentPlayer);
                displayBoxInfo(currentPlayer);

                // Notifica all'utente dell'acquisto avvenuto con successo
                boxInfo.setText("Hai acquistato " + purchasableBox.getName() + " per " + purchasableBox.getPrice() + "$!");
                buyBtn.setDisable(true);
            } catch (NotEnoughMoneyException e) {
                // Gestisci l'eccezione se il giocatore non ha abbastanza soldi
                boxInfo.setText("Non hai abbastanza denaro per acquistare questa proprietà.");

            }
            catch (BoxAlreadyPurchasedException e) {
                boxInfo.setText("Questa proprietà è già stata acquistata.");
            }
        } else {
            // Gestisci il caso in cui la casella non sia acquistabile (non è necessario ma aggiunge sicurezza)
            boxInfo.setText("Questa casella non è acquistabile.");
        }
    }

}
