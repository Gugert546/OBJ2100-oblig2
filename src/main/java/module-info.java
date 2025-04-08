module com.gresskar.trafficsimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gresskar.trafficsimulator to javafx.fxml;
    exports com.gresskar.trafficsimulator;
}