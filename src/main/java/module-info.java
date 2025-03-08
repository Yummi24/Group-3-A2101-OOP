module com.example.motorphoop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.motorphoop to javafx.fxml;
    exports com.example.motorphoop;
}