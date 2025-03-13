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


public class EditEmployeeController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private ScrollPane scrollPane;
    @FXML private VBox employeeListContainer;
    @FXML private Button Employees, Leaverequest, OTrequest, Timestamp, Logout, Edit, Payslip;


    @FXML
    private void handleEmployees(ActionEvent event) throws IOException { switchScene(event, "Employee.fxml"); }
    @FXML private void handleLeaveRequests(ActionEvent event) throws IOException { switchScene(event, "LeaveRequest.fxml"); }
    @FXML private void handleOTRequests(ActionEvent event) throws IOException { switchScene(event, "OTRequest.fxml"); }
    @FXML private void handleTimeStamps(ActionEvent event) throws IOException { switchScene(event, "TimeStamp.fxml"); }
    @FXML private void handleEdit(ActionEvent event) throws IOException { switchScene(event, "EmployeeEDIT.fxml"); }
    @FXML private void handlePayslip(ActionEvent event) throws IOException { switchScene(event, "Payslip.fxml"); }

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

