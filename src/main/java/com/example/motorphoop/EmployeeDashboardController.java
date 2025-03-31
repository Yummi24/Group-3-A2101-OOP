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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class EmployeeDashboardController {


    @FXML private Label nameLabel, employeeIDLabel, positionLabel, netSalaryLabel, phoneLabel, statusLabel;
    @FXML private Label birthdayLabel, addressLabel, tinLabel, sssLabel, philhealthLabel, pagibigLabel, supervisorLabel;

    @FXML private AnchorPane profilePane, requestPane;

    @FXML private DatePicker leaveStartDatePicker, leaveEndDatePicker;
    @FXML private ComboBox<String> leaveTypeComboBox;
    @FXML private TextArea leaveReasonTextArea;
    @FXML private Label leaveStatusLabel;

    @FXML private DatePicker otDatePicker;
    @FXML private ComboBox<String> startTimeComboBox, endTimeComboBox;
    @FXML private TextArea otReasonTextArea;
    @FXML private Label otStatusLabel;

    private String loggedInEmployeeID;

    public void setEmployeeID(String employeeID) {
        this.loggedInEmployeeID = employeeID;
        loadEmployeeData();
        populateComboBoxes();
        restrictDatePickers();
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
                if (details.length < 19) continue;

                String employeeID = details[0].trim();
                if (employeeID.equals(loggedInEmployeeID)) {
                    nameLabel.setText("Name: " + details[2].trim() + " " + details[1].trim());
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
            e.printStackTrace();
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

        for (int hour = 12; hour <= 16; hour++) {  // 12:00 - 16:00 Only
            for (int min = 0; min < 60; min += 30) {
                String time = String.format("%02d:%02d", hour, min);
                startTimeComboBox.getItems().add(time);
                endTimeComboBox.getItems().add(time);
            }
        }
    }

    private void restrictDatePickers() {
        LocalDate today = LocalDate.now();
        leaveStartDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });

        leaveEndDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });

        otDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });
    }

    @FXML
    private void submitOTRequest() {
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.of(16, 0))) {
            showAlert(Alert.AlertType.WARNING, "Booking Closed", "OT booking is closed for today. Try again tomorrow.");
            return;
        }

        if (otDatePicker.getValue() == null || startTimeComboBox.getValue() == null ||
                endTimeComboBox.getValue() == null || otReasonTextArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Form", "Please fill in all fields.");
            return;
        }

        String otDate = otDatePicker.getValue().toString();
        if (isDuplicateRequest(otDate, "OT")) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Request", "You have already submitted an Overtime request on this date.");
            return;
        }

        String csvFile = "src/OT Request.csv";
        long hours = calculateHours(startTimeComboBox.getValue(), endTimeComboBox.getValue());

        try (PrintWriter out = new PrintWriter(new FileWriter(csvFile, true))) {
            out.println(String.join(",", loggedInEmployeeID, nameLabel.getText().replace("Name: ", ""),
                    positionLabel.getText().replace("Position: ", ""), otDate, startTimeComboBox.getValue(),
                    endTimeComboBox.getValue(), String.valueOf(hours), otReasonTextArea.getText().trim(), "Pending"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        otStatusLabel.setText("Pending");
        showAlert(Alert.AlertType.INFORMATION, "Success", "Overtime request submitted successfully.");
    }

    private boolean isDuplicateRequest(String requestDate, String requestType) {
        String csvFile = requestType.equals("Leave") ? "src/Leave Request.csv" : "src/OT Request.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");
                if (details.length < 7) continue;

                String employeeID = details[0].trim();
                String date = requestType.equals("Leave") ? details[5].trim() : details[3].trim();

                if (employeeID.equals(loggedInEmployeeID) && date.equals(requestDate)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private long calculateHours(String start, String end) {
        return Duration.between(LocalTime.parse(start), LocalTime.parse(end)).toHours();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

}
