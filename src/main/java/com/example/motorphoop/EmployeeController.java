package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class EmployeeController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private ScrollPane scrollPane;
    @FXML private VBox employeeListContainer;
    @FXML private Button Employees, Leaverequest, OTrequest, Timestamp, Logout;

    @FXML
    public void initialize() {
        loadEmployeeData();
    }

    // Load Employee Data from CSV using HashMap
    private void loadEmployeeData() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/Employees.csv"))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");
                if (details.length >= 19) {
                    Map<String, String> employeeData = new HashMap<>();
                    employeeData.put("ID", details[0].trim());
                    employeeData.put("Name", details[2].trim() + " " + details[1].trim());
                    employeeData.put("Birthday", details[3].trim());
                    employeeData.put("Address", details[4].trim());
                    employeeData.put("Phone", details[5].trim());
                    employeeData.put("SSS", details[6].trim());
                    employeeData.put("PhilHealth", details[7].trim());
                    employeeData.put("TIN", details[8].trim());
                    employeeData.put("Pagibig", details[9].trim());
                    employeeData.put("Position", details[12].trim());
                    employeeData.put("Supervisor", details[13].trim());

                    // Create Employee Entry (only ID, Name, Position shown)
                    HBox employeeEntry = createEmployeeEntry(
                            employeeData.get("ID"),
                            employeeData.get("Name"),
                            employeeData.get("Position")
                    );

                    employeeEntry.setOnMouseClicked(event -> showEmployeeDetails(employeeData));
                    employeeListContainer.getChildren().add(employeeEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading CSV file.");
        }
    }

    // Create Clickable Employee Entries
    private HBox createEmployeeEntry(String id, String name, String position) {
        HBox hbox = new HBox(20);
        hbox.getStyleClass().add("employee-entry");

        Label idLabel = new Label("ID: " + id);
        Label nameLabel = new Label("Name: " + name);
        Label positionLabel = new Label("Position: " + position);

        hbox.getChildren().addAll(idLabel, nameLabel, positionLabel);
        return hbox;
    }

    // Show Employee Details Page
    private void showEmployeeDetails(Map<String, String> employeeData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeDetails.fxml"));
            Parent root = loader.load();

            EmployeeDetailsController controller = loader.getController();
            controller.displayEmployeeDetails(
                    employeeData.get("ID"),
                    employeeData.get("Name"),
                    employeeData.getOrDefault("Birthday", "N/A"),
                    employeeData.getOrDefault("Address", "N/A"),
                    employeeData.getOrDefault("Phone", "N/A"),
                    employeeData.getOrDefault("SSS", "N/A"),
                    employeeData.getOrDefault("PhilHealth", "N/A"),
                    employeeData.getOrDefault("TIN", "N/A"),
                    employeeData.getOrDefault("Pagibig", "N/A"),
                    employeeData.get("Position"),
                    employeeData.getOrDefault("Supervisor", "N/A")
            );

            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading EmployeeDetails.fxml");
        }
    }

    // Navigation Methods
    @FXML private void handleEmployees(ActionEvent event) throws IOException { switchScene(event, "Employee.fxml"); }
    @FXML private void handleLeaveRequests(ActionEvent event) throws IOException { switchScene(event, "LeaveRequest.fxml"); }
    @FXML private void handleOTRequests(ActionEvent event) throws IOException { switchScene(event, "OTRequest.fxml"); }
    @FXML private void handleTimeStamps(ActionEvent event) throws IOException { switchScene(event, "TimeStamp.fxml"); }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Login.fxml. Check the file path.");
        }
    }

    // Scene Switching Logic
    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}