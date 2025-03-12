package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginButton;

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/motorphoop/Employee.fxml"));

            Scene dashboardScene = new Scene(fxmlLoader.load());

            // Get current stage and switch to dashboard
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
