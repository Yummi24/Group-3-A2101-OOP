package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AddEditController {

    @FXML private TextField searchField, employeeIDField, lastNameField, firstNameField, addressField, phoneField,
            sssField, philhealthField, tinField, pagibigField, statusField,
            positionField, supervisorField, basicSalaryField, riceSubsidyField,
            phoneAllowanceField, clothingAllowanceField, semiMonthlyRateField, hourlyRateField;
    @FXML private Button editButton, addButton, deleteButton; // Renamed saveButton to editButton

    private List<String[]> employees = new ArrayList<>();
    private String selectedEmployeeID;
    private final String FILE_PATH = "src/Employees.csv";

    public void initialize() {
        loadEmployeeData();
        searchField.setOnAction(event -> handleSearch());
    }

    private void loadEmployeeData() {
        employees.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                employees.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading employee data: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchID = searchField.getText().trim();
        if (searchID.isEmpty()) {
            showAlert("Input Required", "Please enter an Employee ID.", Alert.AlertType.WARNING);
            return;
        }

        for (String[] employee : employees) {
            if (employee[0].equals(searchID)) {
                selectedEmployeeID = searchID;
                fillFields(employee);
                return;
            }
        }
        showAlert("Not Found", "No employee found with ID: " + searchID, Alert.AlertType.ERROR);
    }

    private void fillFields(String[] employee) {
        employeeIDField.setText(employee[0]);
        lastNameField.setText(employee[1]);
        firstNameField.setText(employee[2]);
        addressField.setText(employee[4]);
        phoneField.setText(employee[5]);
        sssField.setText(employee[6]);
        philhealthField.setText(employee[7]);
        tinField.setText(employee[8]);
        pagibigField.setText(employee[9]);
        positionField.setText(employee[10]);
        supervisorField.setText(employee[11]);
        basicSalaryField.setText(employee[12]);
        riceSubsidyField.setText(employee[13]);
        phoneAllowanceField.setText(employee[14]);
        clothingAllowanceField.setText(employee[15]);
        semiMonthlyRateField.setText(employee[16]);
        hourlyRateField.setText(employee[17]);
    }

    @FXML
    private void handleEdit() { // Renamed from handleSave
        if (selectedEmployeeID == null) {
            showAlert("No Employee Selected", "Please search and select an employee to edit.", Alert.AlertType.ERROR);
            return;
        }

        for (String[] employee : employees) {
            if (employee[0].equals(selectedEmployeeID)) {
                updateEmployeeData(employee);
                saveToCSV();
                showAlert("Success", "Employee details updated!", Alert.AlertType.INFORMATION);
                return;
            }
        }
        showAlert("Error", "Employee not found.", Alert.AlertType.ERROR);
    }

    private void updateEmployeeData(String[] employee) {
        employee[1] = lastNameField.getText();
        employee[2] = firstNameField.getText();
        employee[4] = addressField.getText();
        employee[5] = phoneField.getText();
        employee[6] = sssField.getText();
        employee[7] = philhealthField.getText();
        employee[8] = tinField.getText();
        employee[9] = pagibigField.getText();
        employee[10] = positionField.getText();
        employee[11] = supervisorField.getText();
        employee[12] = basicSalaryField.getText();
        employee[13] = riceSubsidyField.getText();
        employee[14] = phoneAllowanceField.getText();
        employee[15] = clothingAllowanceField.getText();
        employee[16] = semiMonthlyRateField.getText();
        employee[17] = hourlyRateField.getText();
    }

    @FXML
    private void handleAdd() {
        if (employeeIDField.getText().trim().isEmpty() || firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
            showAlert("Missing Data", "Employee ID, First Name, and Last Name are required.", Alert.AlertType.ERROR);
            return;
        }

        String[] newEmployee = {
                employeeIDField.getText(), lastNameField.getText(), firstNameField.getText(),
                addressField.getText(), phoneField.getText(), sssField.getText(),
                philhealthField.getText(), tinField.getText(), pagibigField.getText(),
                positionField.getText(), supervisorField.getText(),
                basicSalaryField.getText(), riceSubsidyField.getText(),
                phoneAllowanceField.getText(), clothingAllowanceField.getText(),
                semiMonthlyRateField.getText(), hourlyRateField.getText()
        };

        employees.add(newEmployee);
        saveToCSV();
        showAlert("Success", "New employee added!", Alert.AlertType.INFORMATION);
        clearFields();
    }
    private void clearFields() {
        employeeIDField.clear();
        lastNameField.clear();
        firstNameField.clear();
        addressField.clear();
        phoneField.clear();
        sssField.clear();
        philhealthField.clear();
        tinField.clear();
        pagibigField.clear();
        positionField.clear();
        supervisorField.clear();
        basicSalaryField.clear();
        riceSubsidyField.clear();
        phoneAllowanceField.clear();
        clothingAllowanceField.clear();
        semiMonthlyRateField.clear();
        hourlyRateField.clear();
    }

    private void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Employee ID,Last Name,First Name,Birthday,Address,Phone,SSS,PhilHealth,TIN,PagIBIG,Position,Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate\n");
            for (String[] emp : employees) {
                writer.write(String.join(",", emp) + "\n");
            }
        } catch (IOException e) {
            System.err.println("❌ Error saving employee data: " + e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        if (selectedEmployeeID == null) {
            showAlert("No Employee Selected", "Please search and select an employee to delete.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this employee?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            employees.removeIf(emp -> emp[0].equals(selectedEmployeeID));
            saveToCSV();
            showAlert("Success", "Employee deleted.", Alert.AlertType.INFORMATION);
            clearFields();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void handleEmployees(ActionEvent event) throws IOException { switchScene(event, "Employee.fxml"); }
    @FXML private void handleLeaveRequests(ActionEvent event) throws IOException { switchScene(event, "LeaveRequest.fxml"); }
    @FXML private void handleOTRequests(ActionEvent event) throws IOException { switchScene(event, "OTRequest.fxml"); }
    @FXML private void handleTimeStamps(ActionEvent event) throws IOException { switchScene(event, "TimeStamp.fxml"); }
    @FXML private void handleLogout(ActionEvent event) { switchScene(event, "Login.fxml"); }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading " + fxmlFile + ": " + e.getMessage());
        }
    }
}
