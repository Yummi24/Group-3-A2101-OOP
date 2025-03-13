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

    private void loadEmployeeData() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/Employees.csv"))) {
            String headerLine = br.readLine();  // Read header line
            if (headerLine == null) {
                System.out.println("Error: CSV file is empty.");
                return;
            }

            String[] headers = headerLine.split(",");
            Map<String, Integer> columnIndex = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                columnIndex.put(headers[i].trim(), i);
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");

                if (details.length >= headers.length) {
                    String id = details[columnIndex.get("Employee #")].trim();
                    String name = details[columnIndex.get("First Name")].trim() + " " +
                            details[columnIndex.get("Last Name")].trim();
                    String position = details[columnIndex.get("Position")].trim();

                    HBox employeeEntry = createEmployeeEntry(id, name, position);
                    employeeEntry.setOnMouseClicked(event ->
                            showEmployeeDetails(
                                    id, name,
                                    details[columnIndex.get("Birthday")].trim(),
                                    details[columnIndex.get("Address")].trim(),
                                    details[columnIndex.get("Phone Number")].trim(),
                                    details[columnIndex.get("SSS #")].trim(),
                                    details[columnIndex.get("Philhealth #")].trim(),
                                    details[columnIndex.get("TIN #")].trim(),
                                    details[columnIndex.get("Pag-ibig #")].trim(),
                                    position,
                                    details[columnIndex.get("Immediate Supervisor")].trim()
                            )
                    );

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
    private void showEmployeeDetails(String id, String name, String birthday, String address, String phone,
                                     String sss, String philhealth, String tin, String pagibig, String position, String supervisor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeDetails.fxml"));
            Parent root = loader.load();

            EmployeeDetailsController controller = loader.getController();
            controller.displayEmployeeDetails(id, name, birthday, address, phone, sss, philhealth, tin, pagibig, position, supervisor);

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
