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
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    private int playerN;

    private int diceValue;

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

    @FXML
    private Button payPrisonBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dice1ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png"))); //inizializzazione dadi
        dice2ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png")));

        nextBtn.setDisable(true);
        buyBtn.setDisable(true);
        payPrisonBtn.setDisable(true);
    }

    //Logica

    public void setGame(Game game) {
        this.game = game;
        setPlayerN();
        displayPlayerInfo(game.getPlayerInTurn());
        displayBoxInfo(game.getPlayerInTurn());
    }

    //metodo che imposta il numero di pedine
    public void setPlayerN() {
        this.playerN = game.getPlayersN();
        createPlayerTokens();
    }

    //metodo per il passaggio dei turni
    public void nextTurn() {
        game.changeTurn();
        displayPlayerInfo(game.getPlayerInTurn());
        displayBoxInfo(game.getPlayerInTurn());
        diceBtn.setDisable(false);
        nextBtn.setDisable(true);
        buyBtn.setDisable(true);

        //se il giocatore è in prigione attiva il pulsante per uscire pagando
        if (game.getPlayerInTurn().isInPrison())
            payPrisonBtn.setDisable(false);
        else
            payPrisonBtn.setDisable(true);

        //se il giocatore è da 3 turni in prigione obbligalo a pagare per uscire
        if (game.getPlayerInTurn().getTurnInPrison() == 3){
            nextBtn.setDisable(true);
            diceBtn.setDisable(true);
            boxInfo.setText("Sei da tre turni in prigione sei obbligato a pagare");
        }
    }



    //Dadi

    //metodo che restitusci il valore dei dadi
    public int getDiceValue() {
        return diceValue;
    }

    //metodo che gestisce i dadi se sono uguali o non
    public void dicesEquals(int diceValue1, int diceValue2) {
        if (diceValue1 == diceValue2) {
            diceBtn.setDisable(false);  //abilita il bottone
            nextBtn.setDisable(true);   //disabilita il bottone
            buyBtn.setDisable(false);
        }
        else {
            diceBtn.setDisable(true);   // Disabilita il bottone dopo aver mosso la pedina
            nextBtn.setDisable(false);  // Abilita il bottono
            buyBtn.setDisable(false);
        }
    }



    //Display

    //Displays player data in the top textArea
    public void displayPlayerInfo(Player player) {
        String playerInfoStr = player.toString();
        playerInfo.setText(playerInfoStr);
    }

    //Displays the current player's Box infos
    private void displayBoxInfo(Player player) {
        String boxInfoStr = "Ti trovi su:\n" + player.getPosition().getDescription();
        boxInfo.setText(boxInfoStr);
    }

    //metodo per aggiornare il boxInfo
    public void updateBoxInfo(String message) {
        boxInfo.setText(message);
    }



    //metodo per acquistare qualsiasi purchasablebox
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
                createPropertyMarker(game.getPlayerTurn());
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

    //metodo per pagare e uscire di prigione
    public void payToExit() {
        //paga €50 per uscire e esci dalla prigione
        game.getPlayerInTurn().exitFromPrisonByPayment();

        //disabilita il pulsante per pagare una volta uscito di prigione
        payPrisonBtn.setDisable(true);

        //attiva i dadi
        diceBtn.setDisable(false);

        //aggiorna i display
        boxInfo.setText("hai pagato €50 ora puoi muoverti dalla prigione");
        displayPlayerInfo(game.getPlayerInTurn());
    }

    //metodo che lancia e gestisce la grafica dei dadi
    public void launchDices(ActionEvent actionEvent) {

        Player playerInTurn = game.getPlayerInTurn();

        if (playerInTurn.isInPrison()) {
            // Mostra un messaggio o un avviso che il giocatore è in prigione
            boxInfo.setText("Non puoi muoverti mentre sei in prigione. Devi tirare i dadi.");
            nextBtn.setDisable(false);
            payPrisonBtn.setDisable(true);
            playerInTurn.setTurnInPrison();

        }

        Random movement = new Random();
        int x1 = movement.nextInt(200) - 100; //numeri random per lanciare i dadi
        int x2 = movement.nextInt(200) - 100;
        int y1 = movement.nextInt(200) - 100;
        int y2 = movement.nextInt(200) - 100;

        int diceValue1 = Dice.launch();
        int diceValue2 = Dice.launch();
        diceValue = diceValue1 + diceValue2;

        if (diceValue1==diceValue2 && playerInTurn.isInPrison()) playerInTurn.setInPrison(false);

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
           dicesEquals(diceValue1, diceValue2);



        });



        // Avvia la transizione parallela (che avvierà tutte le animazioni)
        parallelTransition.play();
    }

    //metodo per muovere il player
    public void movePlayer(int diceValue) {

        if (game.getPlayerInTurn().isInPrison()) return;

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
        }

        game.movePlayer(diceValue);

        //If the player is on the GoToPrison box
        if(rowPos == maxR-1 && columnPos == 0) {
            rowPos = 0;
            columnPos = maxC-1;
            payPrisonBtn.setDisable(false);
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

        displayBoxInfo(game.getPlayerInTurn());

        //metodo per pagare gli affitti di qualsiasi purchasablebox
        try {
            game.getPlayerInTurn().payRent(this);
        } catch (NotEnoughMoneyException e) {
            throw new RuntimeException(e);
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

    //metodo che posiziona le pedine nel tabellone largo
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

    //metodo che crea e posiziona i marker di acquisto
    public void createPropertyMarker(int playerIndex) {
        // Ottieni il colore del giocatore
        Color playerColor = (Color) playerTokens.get(playerIndex).getFill();
        Circle playerToken = playerTokens.get(playerIndex);


        // Ottieni la posizione attuale del giocatore
        Player currentPlayer = game.getPlayerInTurn();
        Box currentBox = currentPlayer.getPosition();

        // Ottieni la posizione della casella nella griglia
        int rowPos = GridPane.getRowIndex(playerToken);   // Assicurati che la tua classe Box abbia il metodo getRow()
        int columnPos = GridPane.getColumnIndex(playerToken); // Assicurati che la tua classe Box abbia il metodo getColumn()
        int maxC = boardGrid.getColumnCount();
        int maxR = boardGrid.getRowCount();

        if (rowPos == 0 && columnPos < maxC - 1) {
            // Crea un rettangolo che rappresenta la proprietà acquistata
            Rectangle propertyMarker = new Rectangle(128, 20); // Rettangolo 10x10
            propertyMarker.setFill(playerColor); // Imposta il colore del rettangolo

            boardGrid.add(propertyMarker, columnPos, rowPos);
            propertyMarker.setTranslateX(0);  // Sposta il rettangolo verso destra di 30 pixel
            propertyMarker.setTranslateY(-85); // Sposta il rettangolo verso l'alto di 30 pixel
        }
        // Movimento verticale giù nella colonna di destra
        else if (columnPos == maxC - 1 && rowPos < maxR - 1) {
            // Crea un rettangolo che rappresenta la proprietà acquistata
            Rectangle propertyMarker = new Rectangle(20, 125); // Rettangolo 10x10
            propertyMarker.setFill(playerColor); // Imposta il colore del rettangolo

            boardGrid.add(propertyMarker, columnPos, rowPos);
            propertyMarker.setTranslateX(150);  // Sposta il rettangolo verso destra di 30 pixel
            propertyMarker.setTranslateY(0); // Sposta il rettangolo verso l'alto di 30 pixel
        }
        // Movimento orizzontale da destra a sinistra
        else if (rowPos == maxR - 1 && columnPos > 0) {
            // Crea un rettangolo che rappresenta la proprietà acquistata
            Rectangle propertyMarker = new Rectangle(125, 20); // Rettangolo 10x10
            propertyMarker.setFill(playerColor); // Imposta il colore del rettangolo

            boardGrid.add(propertyMarker, columnPos, rowPos);
            propertyMarker.setTranslateX(0);  // Sposta il rettangolo verso destra di 30 pixel
            propertyMarker.setTranslateY(85); // Sposta il rettangolo verso l'alto di 30 pixel
        }
        // Movimento verticale su nella colonna di sinistra
        else if (columnPos == 0 && rowPos > 0) {
            // Crea un rettangolo che rappresenta la proprietà acquistata
            Rectangle propertyMarker = new Rectangle(20, 125); // Rettangolo 10x10
            propertyMarker.setFill(playerColor); // Imposta il colore del rettangolo

            boardGrid.add(propertyMarker, columnPos, rowPos);
            propertyMarker.setTranslateX(-20);  // Sposta il rettangolo verso destra di 30 pixel
            propertyMarker.setTranslateY(0); // Sposta il rettangolo verso l'alto di 30 pixel
        }
    }

}
