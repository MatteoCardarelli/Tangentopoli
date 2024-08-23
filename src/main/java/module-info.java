module com.example.tangentopoli {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;

    opens com.example.tangentopoli to javafx.fxml;
    exports com.example.tangentopoli;
    exports com.example.tangentopoli.controller;  // Esporta il package dei controller
    opens com.example.tangentopoli.controller to javafx.fxml;  // Apre il package al modulo javafx.fxml
}
