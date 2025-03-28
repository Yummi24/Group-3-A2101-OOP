package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

public class EmployeeDashboardController {

    // Profile Pane
    @FXML private Label nameLabel, employeeIDLabel, positionLabel, netSalaryLabel, phoneLabel, statusLabel;
    @FXML private Label birthdayLabel, addressLabel, tinLabel, sssLabel, philhealthLabel, pagibigLabel, supervisorLabel;

    // Request Pane
    @FXML private AnchorPane profilePane, requestPane;

    // Leave Request Fields
    @FXML private DatePicker leaveStartDatePicker, leaveEndDatePicker;
    @FXML private ComboBox<String> leaveTypeComboBox;
    @FXML private TextArea leaveReasonTextArea;
    @FXML private Label leaveStatusLabel;

    // OT Request Fields
    @FXML private DatePicker otDatePicker;
    @FXML private ComboBox<String> startTimeComboBox, endTimeComboBox;
    @FXML private TextArea otReasonTextArea;
    @FXML private Label otStatusLabel;

    private String loggedInEmployeeID;

    public void setEmployeeID(String employeeID) {
        this.loggedInEmployeeID = employeeID;
        loadEmployeeData();
        populateComboBoxes();
        updateRequestStatuses();
    }

    private void loadEmployeeData() {
        String csvFile = "src/Employees.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");
                if (details.length < 19) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String employeeID = details[0].trim();
                if (employeeID.equals(loggedInEmployeeID)) {
                    String lastName = details[1].trim();
                    String firstName = details[2].trim();
                    String fullName = firstName + " " + lastName;

                    nameLabel.setText("Name: " + fullName);
                    employeeIDLabel.setText("Employee ID: " + employeeID);
                    positionLabel.setText("Position: " + details[11].trim());
                    netSalaryLabel.setText("Net Salary: " + details[13].trim());
                    phoneLabel.setText("Phone: " + details[5].trim());
                    statusLabel.setText("Status: " + details[10].trim());
                    birthdayLabel.setText("Birthday: " + details[3].trim());
                    addressLabel.setText("Address: " + details[4].trim());
                    tinLabel.setText("TIN: " + details[8].trim());
                    sssLabel.setText("SSS: " + details[6].trim());
                    philhealthLabel.setText("PhilHealth: " + details[7].trim());
                    pagibigLabel.setText("Pag-IBIG: " + details[9].trim());
                    supervisorLabel.setText("Supervisor: " + details[12].trim());
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading employee data: " + e.getMessage());
        }
    }

    private void populateComboBoxes() {
        leaveTypeComboBox.getItems().addAll(
                "Annual Leave (Holiday Entitlement, Bank Holidays)",
                "Casual Leave",
                "Compassionate Leave (Bereavement Leave, Duvet)",
                "Maternity Leave",
                "Sick Leave",
                "Miscellaneous Leave",
                "Emergency Leave"
        );

        for (int hour = 0; hour < 24; hour++) {
            for (int min = 0; min < 60; min += 30) {
                String time = String.format("%02d:%02d", hour, min);
                startTimeComboBox.getItems().add(time);
                endTimeComboBox.getItems().add(time);
            }
        }
    }

    @FXML
    private void submitLeaveRequest() {
        if (leaveStartDatePicker.getValue() == null || leaveEndDatePicker.getValue() == null ||
                leaveTypeComboBox.getValue() == null || leaveReasonTextArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Form", "Please fill in all fields.");
            return;
        }

        String startDate = leaveStartDatePicker.getValue().toString();
        String endDate = leaveEndDatePicker.getValue().toString();
        String leaveType = leaveTypeComboBox.getValue();
        String reason = leaveReasonTextArea.getText().trim();
        String status = "Pending";

        String csvFile = "src/Leave Request.csv";

        File file = new File(csvFile);
        if (file.exists()) {
            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                out.println(String.join(",", loggedInEmployeeID, nameLabel.getText().replace("Name: ", ""),
                        positionLabel.getText().replace("Position: ", ""), leaveType, reason, startDate, endDate, status));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "File Not Found", "The file 'Leave Request.csv' does not exist.");
        }

        leaveStatusLabel.setText("Pending");
        showAlert(Alert.AlertType.INFORMATION, "Success", "Leave request submitted successfully.");
    }

    @FXML
    private void submitOTRequest() {
        if (otDatePicker.getValue() == null || startTimeComboBox.getValue() == null ||
                endTimeComboBox.getValue() == null || otReasonTextArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Form", "Please fill in all fields.");
            return;
        }

        String otDate = otDatePicker.getValue().toString();
        String startTime = startTimeComboBox.getValue();
        String endTime = endTimeComboBox.getValue();
        String reason = otReasonTextArea.getText().trim();
        String status = "Pending";

        long hours = calculateHours(startTime, endTime);

        String csvFile = "src/OT Request.csv";

        File file = new File(csvFile);
        if (file.exists()) {
            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                out.println(String.join(",", loggedInEmployeeID, nameLabel.getText().replace("Name: ", ""),
                        positionLabel.getText().replace("Position: ", ""), otDate, startTime, endTime, String.valueOf(hours), reason, status));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "File Not Found", "The file 'OT Request.csv' does not exist.");
        }

        otStatusLabel.setText("Pending");
        showAlert(Alert.AlertType.INFORMATION, "Success", "Overtime request submitted successfully.");
    }

    private long calculateHours(String start, String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);
        return Duration.between(startTime, endTime).toHours();
    }

    private void updateRequestStatuses() {
        leaveStatusLabel.setText(getLatestStatus("src/Leave Request.csv"));
        otStatusLabel.setText(getLatestStatus("src/OT Request.csv"));
    }

    private String getLatestStatus(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "Pending";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line, lastStatus = "Pending";
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(loggedInEmployeeID)) {
                    lastStatus = details[details.length - 1];
                }
            }
            return lastStatus;
        } catch (IOException e) {
            return "Pending";
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to confirm, or Cancel to stay logged in.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading Login.fxml. Check the file path.");
            }
        }
    }

    @FXML
    private void switchToRequestPane() {
        profilePane.setVisible(false);
        requestPane.setVisible(true);
    }

    @FXML
    private void switchToProfilePane() {
        requestPane.setVisible(false);
        profilePane.setVisible(true);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
