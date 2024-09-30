package com.example.tangentopoli;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Objects;

public class TangentopoliApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MenuView.fxml")));
        Image icon = new Image(getClass().getResourceAsStream("/img/animationAndGameBaseImages/icon.png"));
        stage.getIcons().add(icon);

        stage.getIcons().add(icon);
        Scene scene = new Scene(root);
        stage.setTitle("Tangentopoli");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
