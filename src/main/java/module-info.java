module com.example.tangentopoli {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tangentopoli to javafx.fxml;
    exports com.example.tangentopoli;
}