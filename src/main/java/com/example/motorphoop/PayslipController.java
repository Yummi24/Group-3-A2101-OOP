package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;


public class PayslipController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox employeeListContainer;
    @FXML private Button Employees, Leaverequest, OTrequest, Timestamp, Logout, Edit, Payslip;
    @FXML private TextArea payslipTextArea;


    @FXML
    public void displayPayslipReceipt() {
        String payslipReceipt = generatePayslipReceipt();
        payslipTextArea.setText(payslipReceipt);
    }

    private String generatePayslipReceipt() {
        StringBuilder receipt = new StringBuilder();

        // Header
        receipt.append("====================================\n");
        receipt.append("            MOTORPH PAYSLIP        \n");
        receipt.append("====================================\n");
        receipt.append("Employee ID: 15\n");
        receipt.append("Name       : Romualdez, Fredrick\n");
        receipt.append("Position   : Account Manager / Accounting\n");
        receipt.append("====================================\n");

        // Earnings
        receipt.append("             EARNINGS               \n");
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10s \n", "Monthly Rate", "₱35,000"));
        receipt.append(String.format(" %-20s %10s \n", "Daily Rate", "₱1,250"));
        receipt.append(String.format(" %-20s %10s \n", "Days Worked", "10"));
        receipt.append(String.format(" %-20s %10s \n", "Overtime", "₱0"));
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10s \n", "GROSS INCOME", "₱26,750"));
        receipt.append("====================================\n");

        // Benefits
        receipt.append("             BENEFITS               \n");
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10s \n", "Rice Subsidy", "₱1,500"));
        receipt.append(String.format(" %-20s %10s \n", "Phone Allowance", "₱2,000"));
        receipt.append(String.format(" %-20s %10s \n", "Clothing Allowance", "₱2,000"));
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10s \n", "TOTAL BENEFITS", "₱4,500"));
        receipt.append("====================================\n");

        // Deductions
        receipt.append("             DEDUCTIONS             \n");
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10s \n", "SSS", "₱900"));
        receipt.append(String.format(" %-20s %10s \n", "PhilHealth", "₱300"));
        receipt.append(String.format(" %-20s %10s \n", "Pag-IBIG", "₱100"));
        receipt.append(String.format(" %-20s %10s \n", "Withholding Tax", "₱1,000"));
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10s \n", "TOTAL DEDUCTIONS", "₱2,300"));
        receipt.append("====================================\n");

        // Summary
        receipt.append("             SUMMARY                 \n");
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10s \n", "Gross Income", "₱26,750"));
        receipt.append(String.format(" %-20s %10s \n", "Total Benefits", "₱4,500"));
        receipt.append(String.format(" %-20s %10s \n", "Total Deductions", "₱2,300"));
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10s \n", "NET INCOME", "₱28,950"));
        receipt.append("====================================\n");

        return receipt.toString();
    }


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

