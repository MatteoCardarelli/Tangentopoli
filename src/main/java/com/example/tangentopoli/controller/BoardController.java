package com.example.tangentopoli.controller;

import com.example.tangentopoli.exception.BoxAlreadyPurchasedException;
import com.example.tangentopoli.exception.MaximumConstructionOnProperty;
import com.example.tangentopoli.exception.NotEnoughMoneyException;
import com.example.tangentopoli.exception.NotYourPropertyException;
import com.example.tangentopoli.model.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BoardController implements Initializable {
    public Label cardTitle;
    public Label cardDescription;
    public Button cardBtn;
    private int playerN;

    private int diceValue;

    private Game game;

    private MediaPlayer mediaPlayer;

    private boolean exitPrisonFlag = false;

    private int diceEqualsCombo = 0;

    @FXML
    private Button nextBtn;

    @FXML
    private TextArea boxInfo;

    @FXML
    private Button actionsBtn;

    @FXML
    private Button buildBtn;

    @FXML
    private Button mortgageBtn;

    @FXML
    private Label winnerLabel;

    @FXML
    private DialogPane actionsDialog;

    @FXML
    private Button buyBtn;

    @FXML
    private Button diceBtn;

    @FXML
    private Button payPrisonBtn;

    @FXML
    private DialogPane endDialog;

    @FXML
    private Label moneyLabel;

    @FXML
    private Rectangle colorBox;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView dice1ImageView;

    @FXML
    private ImageView dice2ImageView;

    @FXML
    private GridPane boardGrid;

    @FXML
    private List<Circle> playerTokens;

    @FXML
    private Slider sliderAudio;

    @FXML
    private DialogPane cardDialog;

    private ImageView cardImg;

    @FXML
    private ImageView imageForScreen;

    @FXML
    private Pane mainPane;

    @FXML
    private ImageView imageRandomized;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeBackgroundMusicAndVolumeControl();
        dice1ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png"))); //inizializzazione dadi
        dice2ImageView.setImage(new Image(getClass().getResourceAsStream("/img/dice1.png")));

        nextBtn.setDisable(true);
        buyBtn.setDisable(true);
        payPrisonBtn.setDisable(true);
        payPrisonBtn.setVisible(false);
        endDialog.setVisible(false);
    }

    //GAME LOGIC & SETUP

    public void setGame(Game game) {
        this.game = game;
        setPlayerN();
        displayPlayerInfo(game.getPlayerInTurn());
        displayBoxInfo(game.getPlayerInTurn());
    }

    //Sets the number of players (and tokens)
    public void setPlayerN() {
        this.playerN = game.getPlayersN();
        createPlayerTokens();
    }

    //Passes to the next turn
    public void nextTurn() {
        if(game.getPlayersN() >= 2) {
            removeAllOverlayRectangles();

            game.changeTurn();
            diceEqualsCombo = 0;
            exitPrisonFlag = false;
            displayPlayerInfo(game.getPlayerInTurn());
            displayBoxInfo(game.getPlayerInTurn());
            diceBtn.setDisable(false);
            nextBtn.setDisable(true);
            buyBtn.setDisable(true);

            //se il giocatore è in prigione attiva il pulsante per uscire pagando
            if (game.getPlayerInTurn().isInPrison()) {
                payPrisonBtn.setDisable(false);
                payPrisonBtn.setVisible(true);
            } else {
                payPrisonBtn.setDisable(true);
                payPrisonBtn.setVisible(false);
            }

            //se il giocatore è da 3 turni in prigione obbligalo a pagare per uscire
            if (game.getPlayerInTurn().getTurnInPrison() == 3){
                nextBtn.setDisable(true);
                diceBtn.setDisable(true);
                updateBoxInfo("Sei da tre turni in prigione sei obbligato a pagare");
            }
        } else {
          disableAllBtns(true);
        }
    }

    //DICES

    //Returns the dice value (int)
    public int getDiceValue() {
        return diceValue;
    }

    //Manage both cases of equals or different dices values
    public void dicesEquals(int diceValue1, int diceValue2) {
        if (exitPrisonFlag) {
            diceBtn.setDisable(true);
            nextBtn.setDisable(false);
            buyBtn.setDisable(false);
        }
        else if (diceValue1 == diceValue2 && !game.getPlayerInTurn().isInPrison()) {
            diceEqualsCombo++;

            if (diceEqualsCombo == 3)
                sendToPrison();
            else{
                diceBtn.setDisable(false);  //abilita il bottone
                nextBtn.setDisable(true);   //disabilita il bottone
                buyBtn.setDisable(false);
            }
        }

        else {
            diceEqualsCombo = 0;
            diceBtn.setDisable(true);   // Disabilita il bottone dopo aver mosso la pedina
            nextBtn.setDisable(false);  // Abilita il bottono
            buyBtn.setDisable(false);
        }
    }

    //Launches the dices and manages the graphics of the animation
    public void launchDices(ActionEvent actionEvent) {
        diceBtn.setDisable(true);
        Player playerInTurn = game.getPlayerInTurn();
        removeAllOverlayRectangles();

        if (playerInTurn.isInPrison()) {
            // Mostra un messaggio o un avviso che il giocatore è in prigione
            updateBoxInfo("Non puoi muoverti mentre sei in prigione. Devi tirare i dadi.");
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

        //se il giocatore è in prigione e fa un lancio doppio esce e non può ritirare
        if (diceValue1==diceValue2 && playerInTurn.isInPrison()){
            playerInTurn.setInPrison(false);

            //disabilita il lancio anche se i valori sono uguali
            diceBtn.setDisable(true);
            nextBtn.setDisable(false);

            exitPrisonFlag = true ;
        }

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
            // Se il giocatore è in prigione, disabilita il lancio di nuovo anche con valori uguali
            if (playerInTurn.isInPrison()) {
                diceBtn.setDisable(true);
                nextBtn.setDisable(false);
            } else {
                // Se il valore dei dadi è lo stesso, verifica se il giocatore può rilanciare
                dicesEquals(diceValue1, diceValue2);
            }
        });

        // Avvia la transizione parallela (che avvierà tutte le animazioni)
        parallelTransition.play();
    }

    //PLAYER MOVEMENT & TOKENS GENERATION

    //Moves the player
    public void movePlayer(int diceValue)  {

        if (game.getPlayerInTurn().isInPrison() || game.getPlayerInTurn().isSkipTurn()) return;

        //Circle playerToken = playerTokens.get(game.getPlayerTurn());
        Circle playerToken = getPlayerTokenFromID(game.getPlayerInTurn().getPlayerID());
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
            columnPos = maxC - 1;
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

        //DA SPOSTARE IN GAME
        //metodo per pagare gli affitti di qualsiasi purchasablebox
        if (game.getPlayerInTurn().getPosition() instanceof PurchasableBox){
            game.getPlayerInTurn().payRent(this);
            game.checkPlayerAfterPayment(game.getPlayerInTurn());
        }
        else if (game.getPlayerInTurn().getPosition() instanceof Tax){
            game.getPlayerInTurn().payTax(this);
            game.checkPlayerAfterPayment(game.getPlayerInTurn());
        }
    }

    private Circle getPlayerTokenFromID(int playerID) {
        Circle playerToken = playerTokens.get(0);
        for(Circle token: playerTokens) {
            if(token.getStyleClass().contains(playerID + "playerToken"))
                playerToken = token;
        }

        return playerToken;
    }

    //Creates the tokens
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

            playerToken.getStyleClass().add(i+1 +"playerToken");
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

    //Sets the tokens' position on the small board
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

    //Sets the tokens' position on the medium board
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

    //Sets the tokens' position on the large board
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

    public void sendToPrison() {
        Player playerInTurn = game.getPlayerInTurn();

        if (diceEqualsCombo==3){
            updateBoxInfo("Hai ottenuto tre doppi consecutivi! Vai direttamente in prigione.");
            diceEqualsCombo = 0;  // Resetta il conteggio dei doppi consecutivi
        } else {
            updateBoxInfo("Sei sato mandato in prigione");
        }

        game.goToPrison(playerInTurn);
        movePlayerToPrison();  // Muovi il giocatore nella casella "In Prigione"
        nextBtn.setDisable(false);
    }

    public void movePlayerToPrison() {
        int prisonRow = 0;   // Coordinata della riga della casella "In Prigione"
        int prisonColumn = boardGrid.getColumnCount() - 1;  // Coordinata della colonna della casella "In Prigione"

        // Aggiorna la posizione della pedina nella casella "In Prigione"
        switch (playerN) {
            case 2, 3:
                //Sincronizza e risolve il conflitto di aggiornamento tra il thread della logica di gioco e il thread dell'interfaccia grafica.
                Platform.runLater(() -> {
                    setPlayerTokenPositionSmall(game.getPlayerTurn(), prisonRow, prisonColumn);
                });
                break;
            case 4, 5:
                setPlayerTokenPositionMedium(game.getPlayerTurn(), prisonRow, prisonColumn);
                break;
            case 6:
                setPlayerTokenPositionLarge(game.getPlayerTurn(), prisonRow, prisonColumn);
                break;
            default:
                break;
        }

    }

    //DISPLAY

    //Displays player data in the top textArea
    public void displayPlayerInfo(Player player) {
        // Ottieni il colore del giocatore
        Circle playerToken = getPlayerTokenFromID(player.getPlayerID());
        Color playerColor = (Color) playerToken.getFill();
        String playerInfoStr[] = player.toStringArray();

        switch(player.getPlayerID()) {
            case 1:imageForScreen.setImage(new Image(getClass().getResourceAsStream("/img/imgForPlayer" + 1 + ".png")));break;
            case 2:imageForScreen.setImage(new Image(getClass().getResourceAsStream("/img/imgForPlayer" + 2 + ".png")));break;
            case 3:imageForScreen.setImage(new Image(getClass().getResourceAsStream("/img/imgForPlayer" + 3 + ".png")));break;
            case 4:imageForScreen.setImage(new Image(getClass().getResourceAsStream("/img/imgForPlayer" + 4 + ".png")));break;
            case 5:imageForScreen.setImage(new Image(getClass().getResourceAsStream("/img/imgForPlayer" + 5 + ".png")));break;
            case 6:imageForScreen.setImage(new Image(getClass().getResourceAsStream("/img/imgForPlayer" + 6 + ".png")));break;
        }

        nameLabel.setText(playerInfoStr[0]);
        moneyLabel.setText(playerInfoStr[1]);
        colorBox.setFill(playerColor);
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

    //Makes the player buy any purchasable box (properties, stations, societies)
    public void buyCurrentBox() {
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
                updateBoxInfo("Hai acquistato " + purchasableBox.getName() + " per " + purchasableBox.getPrice() + "€!");
                createPropertyMarkerFromID(game.getPlayerInTurn().getPlayerID());
                buyBtn.setDisable(true);
            } catch (NotEnoughMoneyException e) {
                // Gestisci l'eccezione se il giocatore non ha abbastanza soldi
                updateBoxInfo(e.getMessage());
            }
            catch (BoxAlreadyPurchasedException e) {
                updateBoxInfo(e.getMessage());
            }
        } else {
            // Gestisci il caso in cui la casella non sia acquistabile (non è necessario ma aggiunge sicurezza)
            updateBoxInfo("Questa casella non è acquistabile.");
        }
    }

    //Makes the player exit from prison after a payment
    public void exitFromPrison()  {
        //paga €50 per uscire ed esci dalla prigione
        game.getPlayerInTurn().exitFromPrison();
        game.checkPlayerAfterPayment(game.getPlayerInTurn());

        //disabilita il pulsante per pagare una volta uscito di prigione
        payPrisonBtn.setDisable(true);
        payPrisonBtn.setVisible(false);

        //attiva i dadi
        diceBtn.setDisable(false);

        //aggiorna i display
        updateBoxInfo("hai pagato €50 ora puoi muoverti dalla prigione");
        displayPlayerInfo(game.getPlayerInTurn());
    }

    //metodo che crea e posiziona i marker di acquisto
    public void createPropertyMarkerFromID(int playerID) {
        // Ottieni il colore del giocatore
        Circle playerToken = getPlayerTokenFromID(playerID);
        Color playerColor = (Color) playerToken.getFill();



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
            propertyMarker.getStyleClass().add(playerID + "propertyMarker");

            boardGrid.add(propertyMarker, columnPos, rowPos);
            propertyMarker.setTranslateX(0);  // Sposta il rettangolo verso destra di 30 pixel
            propertyMarker.setTranslateY(-85); // Sposta il rettangolo verso l'alto di 30 pixel
        }
        // Movimento verticale giù nella colonna di destra
        else if (columnPos == maxC - 1 && rowPos < maxR - 1) {
            // Crea un rettangolo che rappresenta la proprietà acquistata
            Rectangle propertyMarker = new Rectangle(20, 125); // Rettangolo 10x10
            propertyMarker.setFill(playerColor); // Imposta il colore del rettangolo
            propertyMarker.getStyleClass().add(playerID + "propertyMarker");

            boardGrid.add(propertyMarker, columnPos, rowPos);
            propertyMarker.setTranslateX(150);  // Sposta il rettangolo verso destra di 30 pixel
            propertyMarker.setTranslateY(0); // Sposta il rettangolo verso l'alto di 30 pixel
        }
        // Movimento orizzontale da destra a sinistra
        else if (rowPos == maxR - 1 && columnPos > 0) {
            // Crea un rettangolo che rappresenta la proprietà acquistata
            Rectangle propertyMarker = new Rectangle(125, 20); // Rettangolo 10x10
            propertyMarker.setFill(playerColor); // Imposta il colore del rettangolo
            propertyMarker.getStyleClass().add(playerID + "propertyMarker");

            boardGrid.add(propertyMarker, columnPos, rowPos);
            propertyMarker.setTranslateX(0);  // Sposta il rettangolo verso destra di 30 pixel
            propertyMarker.setTranslateY(85); // Sposta il rettangolo verso l'alto di 30 pixel
        }
        // Movimento verticale su nella colonna di sinistra
        else if (columnPos == 0 && rowPos > 0) {
            // Crea un rettangolo che rappresenta la proprietà acquistata
            Rectangle propertyMarker = new Rectangle(20, 125); // Rettangolo 10x10
            propertyMarker.setFill(playerColor); // Imposta il colore del rettangolo
            propertyMarker.getStyleClass().add(playerID + "propertyMarker");

            boardGrid.add(propertyMarker, columnPos, rowPos);
            propertyMarker.setTranslateX(-20);  // Sposta il rettangolo verso destra di 30 pixel
            propertyMarker.setTranslateY(0); // Sposta il rettangolo verso l'alto di 30 pixel
        }
    }

    //Set the Actions DialogPane as visible
    public void showActionsDialog(ActionEvent actionEvent) {
        actionsDialog.setVisible(!actionsDialog.isVisible());
    }

    //Builds on the property that has been directly clicked on the board
    public void buildOnProperty(int row, int col) {
        int pos = convertGridCoordsToPosition(row, col);

        try {
            game.buildOnProperty(pos);
            displayPlayerInfo(game.getPlayerInTurn());
            displayConstructionsOnSmallBoard(row,col);
            updateBoxInfo("Hai costruito su " + game.getBoard().getBoxFromIndex(pos).getName());
        } catch (NotYourPropertyException e) {
            updateBoxInfo("Non possiedi questa proprietà");
        } catch (MaximumConstructionOnProperty e) {
            updateBoxInfo("Hai raggiunto il limite di costruzioni su questa casella");
        } catch (NotEnoughMoneyException e){updateBoxInfo("Non hai abbastanza soldi per costruire");}

        removeAllOverlayRectangles();
    }

    public void displayConstructionsOnSmallBoard(int row, int col){
        int pos = convertGridCoordsToPosition(row, col);
        Property property = (Property) game.getBoard().getBoxFromIndex(pos);
        // Ottieni il numero di case sulla proprietà
        int numberOfHouses = property.getHouseN();
        int numberOfHotels = property.getHotel();
        int maxC = boardGrid.getColumnCount();
        int maxR = boardGrid.getRowCount();

        int posMoveX1 = 90;
        int posMoveX2 = 0;
        int posMoveY1 = 43;
        int posMoveY2 = -43;
        if (row == 0 && col < maxC - 1) {


            // Rimozione delle vecchie casette prima di costruirne di nuove o un albergo
            ObservableList<Node> children = boardGrid.getChildren();
            children.removeIf(node -> {
                Integer colIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);

                // Verifica se il nodo è un'istanza di ImageView e appartiene alla classe "streetwear"
                if (node instanceof ImageView) {
                    ImageView imageView = (ImageView) node;

                    // Controlla se l'ImageView appartiene alla classe "streetwear"
                    return colIndex != null && colIndex == col &&
                            rowIndex != null && rowIndex == row &&
                            "streetwear".equals(imageView.getId());
                }
                return false;
            });

            // Aggiunta di nuove casette o costruzione di un albergo
            if (numberOfHouses < 5 && numberOfHotels == 0) {
                for (int i = 0; i < numberOfHouses; i++) {
                    ImageView buildingImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/privateBox.png")));
                    buildingImageView.setFitHeight(40); // Altezza dell'immagine
                    buildingImageView.setFitWidth(40);  // Larghezza dell'immagine
                    buildingImageView.setRotate(180);
                    buildingImageView.setTranslateX(posMoveX1 - i * 30); // Posiziona in base al numero di casette
                    buildingImageView.setTranslateY(60);
                    buildingImageView.setId("streetwear"); // Imposta l'ID per l'identificazione futura
                    boardGrid.add(buildingImageView, col, row);
                }
            } else if (numberOfHouses == 0 && numberOfHotels == 1) {
                ImageView cantiereImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/cantiereAbusivo.png")));
                cantiereImageView.setFitHeight(50);
                cantiereImageView.setFitWidth(50);
                cantiereImageView.setRotate(180);
                cantiereImageView.setTranslateX(40);
                cantiereImageView.setTranslateY(60);
                cantiereImageView.setId("streetwear"); // Imposta l'ID per l'albergo
                boardGrid.add(cantiereImageView, col, row);
            }


        }
        // Movimento verticale giù nella colonna di destra
        else if (col == maxC - 1 && row < maxR - 1) {

            // Rimozione delle vecchie casette prima di costruirne di nuove o un albergo
            ObservableList<Node> children = boardGrid.getChildren();
            children.removeIf(node -> {
                Integer colIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);

                // Verifica se il nodo è un'istanza di ImageView e appartiene alla classe "streetwear"
                if (node instanceof ImageView) {
                    ImageView imageView = (ImageView) node;

                    // Controlla se l'ImageView appartiene alla classe "streetwear"
                    return colIndex != null && colIndex == col &&
                            rowIndex != null && rowIndex == row &&
                            "streetwear".equals(imageView.getId());
                }
                return false;
            });

            // Aggiunta di nuove casette o costruzione di un albergo
            if (numberOfHouses < 5 && numberOfHotels == 0) {
                for (int i = 0; i < numberOfHouses; i++) {
                    ImageView buildingImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/privateBox.png")));
                    buildingImageView.setFitHeight(40); // Altezza dell'immagine
                    buildingImageView.setFitWidth(40);  // Larghezza dell'immagine
                    buildingImageView.setRotate(270);
                    buildingImageView.setTranslateX(-3); // Posiziona in base al numero di casette
                    buildingImageView.setTranslateY(posMoveY1 - i * 30);
                    buildingImageView.setId("streetwear"); // Imposta l'ID per l'identificazione futura
                    boardGrid.add(buildingImageView, col, row);
                }
            } else if (numberOfHouses == 0 && numberOfHotels == 1) {
                ImageView cantiereImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/cantiereAbusivo.png")));
                cantiereImageView.setFitHeight(50);
                cantiereImageView.setFitWidth(50);
                cantiereImageView.setRotate(270);
                cantiereImageView.setTranslateX(0);
                cantiereImageView.setTranslateY(0);
                cantiereImageView.setId("streetwear"); // Imposta l'ID per l'albergo
                boardGrid.add(cantiereImageView, col, row);
            }

        }
        // Movimento orizzontale da destra a sinistra
        else if (row == maxR - 1 && col > 0) {

            // Rimozione delle vecchie casette prima di costruirne di nuove o un albergo
            ObservableList<Node> children = boardGrid.getChildren();
            children.removeIf(node -> {
                Integer colIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);

                // Verifica se il nodo è un'istanza di ImageView e appartiene alla classe "streetwear"
                if (node instanceof ImageView) {
                    ImageView imageView = (ImageView) node;

                    // Controlla se l'ImageView appartiene alla classe "streetwear"
                    return colIndex != null && colIndex == col &&
                            rowIndex != null && rowIndex == row &&
                            "streetwear".equals(imageView.getId());
                }
                return false;
            });

            // Aggiunta di nuove casette o costruzione di un albergo
            if (numberOfHouses < 5 && numberOfHotels == 0) {
                for (int i = 0; i < numberOfHouses; i++) {
                    ImageView buildingImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/privateBox.png")));
                    buildingImageView.setFitHeight(40); // Altezza dell'immagine
                    buildingImageView.setFitWidth(40);  // Larghezza dell'immagine
                    buildingImageView.setRotate(0);
                    buildingImageView.setTranslateX(posMoveX2 + i * 30); // Posiziona in base al numero di casette
                    buildingImageView.setTranslateY(-60);
                    buildingImageView.setId("streetwear"); // Imposta l'ID per l'identificazione futura
                    boardGrid.add(buildingImageView, col, row);
                }
            } else if (numberOfHouses == 0 && numberOfHotels == 1) {
                ImageView cantiereImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/cantiereAbusivo.png")));
                cantiereImageView.setFitHeight(50);
                cantiereImageView.setFitWidth(50);
                cantiereImageView.setRotate(180);
                cantiereImageView.setTranslateX(40);
                cantiereImageView.setTranslateY(-60);
                cantiereImageView.setId("streetwear"); // Imposta l'ID per l'albergo
                boardGrid.add(cantiereImageView, col, row);
            }

        }
        // Movimento verticale su nella colonna di sinistra
        else if (col == 0 && row > 0) {
            // Rimozione delle vecchie casette prima di costruirne di nuove o un albergo
            ObservableList<Node> children = boardGrid.getChildren();
            children.removeIf(node -> {
                Integer colIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);

                // Verifica se il nodo è un'istanza di ImageView e appartiene alla classe "streetwear"
                if (node instanceof ImageView) {
                    ImageView imageView = (ImageView) node;

                    // Controlla se l'ImageView appartiene alla classe "streetwear"
                    return colIndex != null && colIndex == col &&
                            rowIndex != null && rowIndex == row &&
                            "streetwear".equals(imageView.getId());
                }
                return false;
            });

            // Aggiunta di nuove casette o costruzione di un albergo
            if (numberOfHouses < 5 && numberOfHotels == 0) {
                for (int i = 0; i < numberOfHouses; i++) {
                    ImageView buildingImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/privateBox.png")));
                    buildingImageView.setFitHeight(40); // Altezza dell'immagine
                    buildingImageView.setFitWidth(40);  // Larghezza dell'immagine
                    buildingImageView.setRotate(90);
                    buildingImageView.setTranslateX(110); // Posiziona in base al numero di casette
                    buildingImageView.setTranslateY(posMoveY2 + i * 30);
                    buildingImageView.setId("streetwear"); // Imposta l'ID per l'identificazione futura
                    boardGrid.add(buildingImageView, col, row);
                }
            } else if (numberOfHouses == 0 && numberOfHotels == 1) {
                ImageView cantiereImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/cantiereAbusivo.png")));
                cantiereImageView.setFitHeight(50);
                cantiereImageView.setFitWidth(50);
                cantiereImageView.setRotate(90);
                cantiereImageView.setTranslateX(110);
                cantiereImageView.setTranslateY(0);
                cantiereImageView.setId("streetwear"); // Imposta l'ID per l'albergo
                boardGrid.add(cantiereImageView, col, row);
            }

        }

    }

    //Creates the green overlays on the buildable properties
    public void createConstructionOverlay(int row, int col) {
        // Crea il rettangolo semi-trasparente
        Rectangle overlay = new Rectangle(126, 150);
        overlay.setStyle("-fx-fill: rgba(26,255,0,0.5);");
        overlay.getStyleClass().add("property-overlay");

        // Se il rettangolo si trova sulle colonne laterali, ruotalo di 90 gradi
        if (col == 0 || col == boardGrid.getColumnCount() - 1) {
            overlay.setRotate(90);
        }

        // Aggiungi la StackPane al GridPane
        boardGrid.add(overlay, col, row);

        // Applica l'animazione di lampeggio
        applyBlinkingEffect(overlay);

        overlay.setOnMouseClicked(event -> {
            buildOnProperty(row, col);
        });
    }

    //Creates the blinking effect on the overlay
    private void applyBlinkingEffect(Rectangle overlay) {
        // Creazione dell'animazione di fade in/out per il lampeggio
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), overlay);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.2);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    //Converts the position of a specific Box (giving its index in the board), into coordinates
    public int[] convertPositionToGridCoords(int index) {
        int maxC =  boardGrid.getColumnCount();
        int maxR = boardGrid.getRowCount();
        int totalPerimeter = 2 * (maxC + maxR - 2);
        int row = 0;
        int col = 0;

        if (index < maxC) {
            // Parte superiore (da sinistra a destra)
            row = 0;
            col = index;
        } else if (index < maxC + maxR - 1) {
            // Lato destro (dall'alto verso il basso)
            row = index - maxC + 1;
            col = maxC - 1;
        } else if (index < 2 * maxC + maxR - 2) {
            // Parte inferiore (da destra a sinistra)
            row = maxR - 1;
            col = (maxC - 1) - (index - (maxC + maxR - 2));
        } else if (index < totalPerimeter) {
            // Lato sinistro (dal basso verso l'alto)
            row = (maxR - 1) - (index - (2 * maxC + maxR - 3));
            col = 0;
        }
        return new int[] { row, col };
    }

    public int convertGridCoordsToPosition(int row, int col) {
        int maxC = boardGrid.getColumnCount();
        int maxR = boardGrid.getRowCount();
        int totalPerimeter = 2 * (maxC + maxR - 2);

        // Verifica che le coordinate siano valide
        if (row < 0  || row >= maxR || col < 0 || col >= maxC) {
            throw new IllegalArgumentException("Coordinate fuori dai limiti della griglia");
        }

        int index = 0;

        if (row == 0) {
            // Parte superiore (da sinistra a destra)
            index = col;
        } else if (col == maxC - 1) {
            // Lato destro (dall'alto verso il basso)
            index = maxC - 1 + row;
        } else if (row == maxR - 1) {
            // Parte inferiore (da destra a sinistra)
            index = (maxC - 1) + (maxR - 1) + (maxC - 1 - col);
        } else if (col == 0) {
            // Lato sinistro (dal basso verso l'alto)
            index = totalPerimeter - row;
        }

        return index;
    }

    //Once the Construct button has been clicked, the method puts the overlays on all the buildable properties
    public void buildBtnClick(ActionEvent actionEvent) {
        removeAllOverlayRectangles();
        actionsDialog.setVisible(false);
        updateBoxInfo("Clicca sulle proprietà su cui vuoi costruire");
        ArrayList<Integer> buildablePropertiesIndex = game.getBuildableProperties();
        int[] coords = {0, 0};
        for (int i = 0; i < buildablePropertiesIndex.size(); i++) {
            coords = convertPositionToGridCoords(buildablePropertiesIndex.get(i));
            createConstructionOverlay(coords[0], coords[1]);
        }
    }

    public void mortgageBox(int row, int col) {
        int pos = convertGridCoordsToPosition(row, col);
            game.mortgageBox(pos);
            displayPlayerInfo(game.getPlayerInTurn());
            updateBoxInfo("Hai ipotecato " + game.getBoard().getBoxFromIndex(pos).getName());
        removeAllOverlayRectangles();
        createNotAvailableBoxOverlay(row, col);

    }

    public void createMortgageOverlay(int row, int col) {
        // Crea il rettangolo semi-trasparente
        Rectangle overlay = new Rectangle(126, 150);
        overlay.setStyle("-fx-fill: rgba(255,0,0,0.5);");
        overlay.getStyleClass().add("property-overlay");
        overlay.getStyleClass().add(game.getPlayerInTurn().getPlayerID() + "mortOverlay");

        // Se il rettangolo si trova sulle colonne laterali, ruotalo di 90 gradi
        if (col == 0 || col == boardGrid.getColumnCount() - 1) {
            overlay.setRotate(90);
            overlay.setTranslateX(12);
        }

        // Aggiungi la StackPane al GridPane
        boardGrid.add(overlay, col, row);

        // Applica l'animazione di lampeggio
        applyBlinkingEffect(overlay);

        overlay.setOnMouseClicked(event -> {
            mortgageBox(row, col);
        });
    }

    private void createNotAvailableBoxOverlay(int row, int col) {
        // Crea il rettangolo semi-trasparente
        Rectangle overlay = new Rectangle(126, 150);
        overlay.setStyle("-fx-fill: rgba(48,46,46,0.5);");
        overlay.getStyleClass().add("notAvailableBoxOverlay");

        // Se il rettangolo si trova sulle colonne laterali, ruotalo di 90 gradi
        if (col == 0 || col == boardGrid.getColumnCount() - 1) {
            overlay.setRotate(90);
            overlay.setTranslateX(12);
        }

        // Aggiungi la StackPane al GridPane
        boardGrid.add(overlay, col, row);
    }

    public void mortgageBtnClick(ActionEvent actionEvent) {
        removeAllOverlayRectangles();
        actionsDialog.setVisible(false);
        updateBoxInfo("Clicca sulle caselle che vuoi ipotecare");
        ArrayList<Integer> mortgageableProperties = game.getMortgageableBoxes();
        int[] coords = {0, 0};
        for (int i = 0; i < mortgageableProperties.size(); i++) {
            coords = convertPositionToGridCoords(mortgageableProperties.get(i));
            createMortgageOverlay(coords[0], coords[1]);
        }
    }

    //Removes all the overlays on the properties
    public void removeAllOverlayRectangles() {
        // Ottieni la lista di tutti i figli del GridPane
        ObservableList<Node> children = boardGrid.getChildren();

        // Usa un iteratore per evitare ConcurrentModificationException
        Iterator<Node> iterator = children.iterator();

        while (iterator.hasNext()) {
            Node child = iterator.next();

            // Controlla se il figlio è un'istanza di Rectangle
            if (child instanceof Rectangle && child.getStyleClass().contains("property-overlay")) {
                iterator.remove(); // Rimuovi il rettangolo dalla griglia
            }
        }
    }

    public void removeNotAvailableBoxOverlay(int row, int col) {
        // Ottieni la lista di tutti i figli del GridPane
        ObservableList<Node> children = boardGrid.getChildren();

        // Usa un iteratore per evitare ConcurrentModificationException
        Iterator<Node> iterator = children.iterator();

        while (iterator.hasNext()) {
            Node child = iterator.next();

            // Verifica se il nodo si trova nella cella specificata
            Integer nodeRow = GridPane.getRowIndex(child);
            Integer nodeCol = GridPane.getColumnIndex(child);

            // Assicura che rowIndex e colIndex non siano null (valore di default se non impostato è 0)
            if (nodeRow == null) nodeRow = 0;
            if (nodeCol == null) nodeCol = 0;

            // Controlla se il figlio è un'istanza di Rectangle e si trova nella cella specificata
            if (child instanceof Rectangle && child.getStyleClass().contains("notAvailableBoxOverlay")
                    && nodeRow == row && nodeCol == col) {
                iterator.remove(); // Rimuovi il rettangolo dalla griglia
                break; // Esci dal loop una volta trovato e rimosso l'overlay
            }
        }
    }

    public void demolishBtnClick(ActionEvent actionEvent) {
        removeAllOverlayRectangles();
        actionsDialog.setVisible(false);
        updateBoxInfo("Clicca sulle proprietà su cui vuoi demolire");
        ArrayList<Integer> demolishablePropertiesIndex = game.getDemolishableProperties();
        int[] coords = {0, 0};
        for (int i = 0; i < demolishablePropertiesIndex.size(); i++) {
            coords = convertPositionToGridCoords(demolishablePropertiesIndex.get(i));
            createDemolitionOverlay(coords[0], coords[1]);
        }
    }

    public void createDemolitionOverlay(int row, int col) {
        // Crea il rettangolo semi-trasparente
        Rectangle overlay = new Rectangle(126, 150);
        overlay.setStyle("-fx-fill: rgba(26,255,0,0.5);");
        overlay.getStyleClass().add("property-overlay");

        // Aggiungi la StackPane al GridPane
        boardGrid.add(overlay, col, row);

        // Se il rettangolo si trova sulle colonne laterali, ruotalo di 90 gradi
        if (col == 0 || col == boardGrid.getColumnCount() - 1) {
            overlay.setRotate(90);
            overlay.setTranslateX(12);
        }



        // Applica l'animazione di lampeggio
        applyBlinkingEffect(overlay);

        overlay.setOnMouseClicked(event -> {
            demolishOnProperty(row, col);
        });
    }

    public void demolishOnProperty(int row, int col) {
        int pos = convertGridCoordsToPosition(row, col);

        game.demolishOnProperty(pos);
        displayPlayerInfo(game.getPlayerInTurn());
        displayConstructionsOnSmallBoard(row,col);
        updateBoxInfo("Hai demolito su " + game.getBoard().getBoxFromIndex(pos).getName());

        removeAllOverlayRectangles();
    }

    public void createExtinguishableMortgageOverlay(int row, int col) {
        // Crea il rettangolo semi-trasparente
        Rectangle overlay = new Rectangle(126, 150);
        overlay.setStyle("-fx-fill: rgba(255,0,0,0.5);");
        overlay.getStyleClass().add("property-overlay");

        // Se il rettangolo si trova sulle colonne laterali, ruotalo di 90 gradi
        if (col == 0 || col == boardGrid.getColumnCount() - 1) {
            overlay.setRotate(90);
        }

        // Aggiungi la StackPane al GridPane
        boardGrid.add(overlay, col, row);

        // Applica l'animazione di lampeggio
        applyBlinkingEffect(overlay);

        overlay.setOnMouseClicked(event -> {
            extinguishMortgageBox(row, col);
        });
    }

    public void extinguishMortgageBox(int row, int col) {
        int pos = convertGridCoordsToPosition(row, col);
        game.extinguishMortgageBox(pos);
        displayPlayerInfo(game.getPlayerInTurn());
        updateBoxInfo("Hai ipotecato " + game.getBoard().getBoxFromIndex(pos).getName());
        removeAllOverlayRectangles();
        removeNotAvailableBoxOverlay(row, col);
    }

    public void extinguishBtnClick(ActionEvent actionEvent) {
        removeAllOverlayRectangles();
        actionsDialog.setVisible(false);
        updateBoxInfo("Clicca sulle caselle sulle quali vuoi estinguere l'ipoteca");
        ArrayList<Integer> extinguishableProperties = game.getExtinguishableBoxes();
        int[] coords = {0, 0};
        for (int i = 0; i < extinguishableProperties.size(); i++) {
            coords = convertPositionToGridCoords(extinguishableProperties.get(i));
            createExtinguishableMortgageOverlay(coords[0], coords[1]);
        }
    }

    public void initializeBackgroundMusicAndVolumeControl() {

        // Inizializza la musica di sottofondo
        String musicFile = getClass().getResource("/audio/madunina.mp3").toExternalForm();
        Media sound = new Media(musicFile);
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Riproduzione in loop
        mediaPlayer.setVolume(0.5); // Volume iniziale al 50%
        mediaPlayer.play(); // Avvia la riproduzione


        sliderAudio.setShowTickLabels(true);
        sliderAudio.setShowTickMarks(true);

        // Collega lo slider al volume del MediaPlayer
        sliderAudio.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue() / 100.0); // Imposta il volume in base al valore dello slider
        });

    }

    public void openCardDialog() {
        cardDialog.setVisible(true);
    }

    public void closeCardDialog(ActionEvent actionEvent) {
        cardDialog.setVisible(false);
    }

    public void setCardInfo(Card card) {

        cardDialog.setScaleY(0);
        Random imageRandom = new Random();
        int randomized = imageRandom.nextInt(2) + 1;

        imageRandomized.setImage(new Image(getClass().getResourceAsStream("/img/cardImage"+randomized+".png")));

        // Crea l'immagine della carta se non esiste già
        if (cardImg == null) {
            cardImg = new ImageView();
            cardImg.setFitWidth(130); // Imposta la larghezza dell'immagine
            cardImg.setPreserveRatio(true); // Mantiene le proporzioni dell'immagine
            mainPane.getChildren().add(cardImg); // Aggiungi l'immagine al layout principale
        }
        if (card instanceof CommunityChest) {
            // Imposta l'immagine della carta
            cardImg.setImage(new Image(getClass().getResourceAsStream("/img/chanceimgop.png")));
            cardImg.setX(240);
            cardImg.setY(250);
            cardImg.setRotate(135);

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), cardImg);
            translateTransition1.setToX(-1500); // Imposta la distanza X di traslazione
            translateTransition1.setToY(274); // Imposta la distanza Y di traslazione
            translateTransition1.setDelay(Duration.millis(200));

            // Seconda animazione: ingrandimento dell'immagine
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.01), cardImg);
            scaleTransition.setToX(4.3); // Ingrandisci l'immagine (2x)
            scaleTransition.setToY(4.3);

            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.01), cardImg);
            rotateTransition.setToAngle(0);

            TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.3), cardImg);
                translateTransition2.setDelay(Duration.millis(500));
                translateTransition2.setToX(505); // Imposta la distanza X di traslazione
                translateTransition2.setToY(162); // Imposta la distanza Y di traslazione

            ScaleTransition scaleTransitionImg = new ScaleTransition(Duration.seconds(0.3), cardImg);
            scaleTransitionImg.setDelay(Duration.millis(300));
            scaleTransitionImg.setToY(0);

            ScaleTransition scaleTransitionDialog = new ScaleTransition(Duration.seconds(0.3), cardDialog);
            scaleTransitionDialog.setToY(1);

            SequentialTransition sequentialTransition = new SequentialTransition(
                    translateTransition1,
                    scaleTransition,
                    rotateTransition,
                    translateTransition2,
                    scaleTransitionImg,
                    scaleTransitionDialog
            );

                sequentialTransition.play();

            } else {
            // Imposta l'immagine della carta
            cardImg.setImage(new Image(getClass().getResourceAsStream("/img/communitychestop.png")));
            cardImg.setX(550);
            cardImg.setY(570);
            cardImg.setRotate(135);

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), cardImg);
            translateTransition1.setToX(-1500); // Imposta la distanza X di traslazione
            translateTransition1.setToY(274); // Imposta la distanza Y di traslazione
            translateTransition1.setDelay(Duration.millis(200));

            // Seconda animazione: ingrandimento dell'immagine
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.01), cardImg);
            scaleTransition.setToX(4.3); // Ingrandisci l'immagine (2x)
            scaleTransition.setToY(4.3);

            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.01), cardImg);
            rotateTransition.setToAngle(0);

            TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.3), cardImg);
            translateTransition2.setDelay(Duration.millis(500));
            translateTransition2.setToX(196); // Imposta la distanza X di traslazione
            translateTransition2.setToY(-154); // Imposta la distanza Y di traslazione

            ScaleTransition scaleTransitionImg = new ScaleTransition(Duration.seconds(0.3), cardImg);
            scaleTransitionImg.setDelay(Duration.millis(300));
            scaleTransitionImg.setToY(0);

            ScaleTransition scaleTransitionDialog = new ScaleTransition(Duration.seconds(0.3), cardDialog);
            scaleTransitionDialog.setToY(1);

            SequentialTransition sequentialTransition = new SequentialTransition(
                    translateTransition1,
                    scaleTransition,
                    rotateTransition,
                    translateTransition2,
                    scaleTransitionImg,
                    scaleTransitionDialog
            );


            sequentialTransition.play();

        }

        cardImg = null;


        openCardDialog();
        cardTitle.setText(card.getTitle());
        cardDescription.setText(card.getDescription());
        }

    public void loseGame(Player player) {
        System.out.println("Loser id: " + player.getPlayerID());
        deletePropertyMarkersAndMortgageOverlays(player.getPlayerID());
        deletePlayerTokenFromID(player.getPlayerID());
        System.out.println("Cambio turno dopo perdita");
        nextTurn();
    }

    private void deletePlayerTokenFromID(int playerID) {
        // Ottieni la lista di tutti i figli del GridPane
        ObservableList<Node> children = boardGrid.getChildren();

        // Usa un iteratore per evitare ConcurrentModificationException
        Iterator<Node> iterator = children.iterator();
        Circle playerToken = getPlayerTokenFromID(playerID);
        playerTokens.remove(playerToken);

        while (iterator.hasNext()) {
            Node child = iterator.next();

            // Verifica se il nodo si trova nella cella specificata
            Integer nodeRow = GridPane.getRowIndex(child);
            Integer nodeCol = GridPane.getColumnIndex(child);

            // Assicura che rowIndex e colIndex non siano null (valore di default se non impostato è 0)
            if (nodeRow == null) nodeRow = 0;
            if (nodeCol == null) nodeCol = 0;

            // Controlla se il figlio è un'istanza di Rectangle e si trova nella cella specificata
            if (child instanceof Circle && child.getStyleClass().contains(playerID + "playerToken")) {
                iterator.remove();// Rimuovi il rettangolo dalla griglia
                break;
            }
        }
    }

    private void deletePropertyMarkersAndMortgageOverlays(int playerID) {
        // Ottieni la lista di tutti i figli del GridPane
        ObservableList<Node> children = boardGrid.getChildren();

        // Usa un iteratore per evitare ConcurrentModificationException
        Iterator<Node> iterator = children.iterator();

        while (iterator.hasNext()) {
            Node child = iterator.next();

            // Verifica se il nodo si trova nella cella specificata
            Integer nodeRow = GridPane.getRowIndex(child);
            Integer nodeCol = GridPane.getColumnIndex(child);

            // Assicura che rowIndex e colIndex non siano null (valore di default se non impostato è 0)
            if (nodeRow == null) nodeRow = 0;
            if (nodeCol == null) nodeCol = 0;

            // Controlla se il figlio è un'istanza di Rectangle e si trova nella cella specificata
            if (child instanceof Rectangle && (child.getStyleClass().contains(playerID + "propertyMarker") || child.getStyleClass().contains(playerID + "mortOverlay"))) {
                iterator.remove(); // Rimuovi il rettangolo dalla griglia
            }
        }
    }

    public void gameEnd() {
        updateBoxInfo("bro hai vinto");
        disableAllBtns(true);
        winnerLabel.setText("Giocatore " + game.getPlayers().get(0).getPlayerID() + " hai vinto! complimenti");
        endDialog.setVisible(true);
    }

    public void closeGames(ActionEvent actionEvent) {
        Stage stage = (Stage) boardGrid.getScene().getWindow();
        stage.close();
        try {
            // Carica il file FXML della seconda finestra
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuView.fxml"));
            Parent root = loader.load();

            // Ottieni il controller della seconda finestra, se necessario
            //SecondWindowController controller = loader.getController();

            // Crea una nuova scena con il contenuto caricato
            Scene scene = new Scene(root);

            // Crea un nuovo stage (una nuova finestra)
            stage = new Stage();
            stage.setScene(scene);

            // Mostra la nuova finestra
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disableAllBtns(boolean setDisable) {
        buyBtn.setDisable(setDisable);
        actionsBtn.setDisable(setDisable);
        diceBtn.setDisable(setDisable);
        nextBtn.setDisable(setDisable);
        payPrisonBtn.setDisable(setDisable);
    }

}