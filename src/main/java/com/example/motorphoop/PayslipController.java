package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class PayslipController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private Button Employees, Leaverequest, OTrequest, Timestamp, Logout, Edit, Payslip, Calculate;

    @FXML private Text employeeNameLabel;
    @FXML private Text employeePositionLabel;
    @FXML private Text employeeIDLabel;
    @FXML private TextArea payslipTextArea;

    // Employee data storage
    private Map<String, String> selectedEmployee;

    // Display Payslip Receipt
    @FXML
    public void displayPayslipReceipt() {
        if (selectedEmployee == null) {
            payslipTextArea.setText("Please select an employee first.");
            return;
        }

        String payslipReceipt = generatePayslipReceipt(selectedEmployee);
        payslipTextArea.setText(payslipReceipt);
    }

    // Generate Payslip Details
    private String generatePayslipReceipt(Map<String, String> employeeData) {
        StringBuilder receipt = new StringBuilder();

        double basicSalary = Double.parseDouble(employeeData.get("Basic Salary"));
        double riceSubsidy = Double.parseDouble(employeeData.get("Rice Subsidy"));
        double phoneAllowance = Double.parseDouble(employeeData.get("Phone Allowance"));
        double clothingAllowance = Double.parseDouble(employeeData.get("Clothing Allowance"));

        double grossIncome = basicSalary;
        double totalBenefits = riceSubsidy + phoneAllowance + clothingAllowance;
        double totalDeductions = 2300.00;
        double netIncome = grossIncome + totalBenefits - totalDeductions;

        // Header
        receipt.append("====================================\n");
        receipt.append("            MOTORPH PAYSLIP        \n");
        receipt.append("====================================\n");
        receipt.append("Employee ID: ").append(employeeData.get("ID")).append("\n");
        receipt.append("Name       : ").append(employeeData.get("Name")).append("\n");
        receipt.append("Position   : ").append(employeeData.get("Position")).append("\n");
        receipt.append("====================================\n");

        // Earnings
        receipt.append("             EARNINGS               \n");
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10.2f \n", "Basic Salary", basicSalary));
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10.2f \n", "GROSS INCOME", grossIncome));
        receipt.append("====================================\n");

        // Benefits
        receipt.append("             BENEFITS               \n");
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10.2f \n", "Rice Subsidy", riceSubsidy));
        receipt.append(String.format(" %-20s %10.2f \n", "Phone Allowance", phoneAllowance));
        receipt.append(String.format(" %-20s %10.2f \n", "Clothing Allowance", clothingAllowance));
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10.2f \n", "TOTAL BENEFITS", totalBenefits));
        receipt.append("====================================\n");

        // Deductions
        receipt.append("             DEDUCTIONS             \n");
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10.2f \n", "TOTAL DEDUCTIONS", totalDeductions));
        receipt.append("====================================\n");

        // Summary
        receipt.append("             SUMMARY                 \n");
        receipt.append("------------------------------------\n");
        receipt.append(String.format(" %-20s %10.2f \n", "Gross Income", grossIncome));
        receipt.append(String.format(" %-20s %10.2f \n", "Total Benefits", totalBenefits));
        receipt.append(String.format(" %-20s %10.2f \n", "Net Income", netIncome));
        receipt.append("====================================\n");

        return receipt.toString();
    }

    // Navigation Methods
    @FXML private void handleEmployees(ActionEvent event) throws IOException { switchScene(event, "Employee.fxml"); }
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
