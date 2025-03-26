package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private Stage stage;
    private Scene scene;

    @FXML
    public void initialize() {
        // Allow pressing Enter to log in
        usernameField.setOnAction(event -> onLoginButtonClick(event));
        passwordField.setOnAction(event -> onLoginButtonClick(event));
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Username and password cannot be empty.");
            return;
        }

        String role = authenticateUser(username, password);

        if (role != null) {
            switchDashboard(event, role);
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private String authenticateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/Users.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 4) {
                    String storedUsername = details[1].trim();
                    String storedPassword = details[2].trim();
                    String storedRole = details[3].trim();

                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        return storedRole; // Return role if credentials match
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if authentication fails
    }

    private void switchDashboard(ActionEvent event, String role) {
        String fxmlFile = role.equalsIgnoreCase("HR") ? "Employee.fxml" : "EmployeeDashboard.fxml";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
