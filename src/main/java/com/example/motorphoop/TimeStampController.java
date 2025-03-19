package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TimeStampController {

    @FXML private VBox attendanceContainer;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize() {
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        int recordLimit = 50; // Limit records for better performance
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("src/Attendance Record.csv"))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null && count < recordLimit) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");
                if (details.length >= 6) {
                    HBox recordBox = createAttendanceBox(details);
                    attendanceContainer.getChildren().add(recordBox);
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error loading attendance record data.");
        }
    }


    // ✅ NEW: Improved layout for attendance records
    // Create HBox to resemble Employee List design
    private HBox createAttendanceBox(String[] details) {
        HBox box = new HBox(10);  // Spacing for clean layout
        box.setStyle("-fx-border-color: #1f4a8d; -fx-border-width: 1; -fx-padding: 5;");

        // Fixed Widths for Consistency
        Label lblID = new Label(details[0]);
        lblID.setMinWidth(80);

        Label lblName = new Label(details[1] + " " + details[2]);
        lblName.setMinWidth(200);

        Label lblDate = new Label(details[3]);
        lblDate.setMinWidth(120);

        Label lblLogIn = new Label(details[4]);
        lblLogIn.setMinWidth(80);

        Label lblLogOut = new Label(details[5]);
        lblLogOut.setMinWidth(80);

        String totalHours = calculateTotalHours(details[4], details[5]);
        Label lblTotalHours = new Label(totalHours);
        lblTotalHours.setMinWidth(100);

        box.getChildren().addAll(lblID, lblName, lblDate, lblLogIn, lblLogOut, lblTotalHours);
        return box;
    }


    // ✅ Total hours calculation for better detail
    private String calculateTotalHours(String logIn, String logOut) {
        try {
            String[] logInTime = logIn.split(":");
            String[] logOutTime = logOut.split(":");

            int inHour = Integer.parseInt(logInTime[0]);
            int inMin = Integer.parseInt(logInTime[1]);

            int outHour = Integer.parseInt(logOutTime[0]);
            int outMin = Integer.parseInt(logOutTime[1]);

            int totalMinutes = ((outHour * 60) + outMin) - ((inHour * 60) + inMin);
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;

            return String.format("%02d:%02d", hours, minutes);
        } catch (Exception e) {
            return "N/A";
        }
    }

    // Navigation Methods
    @FXML
    private void handleEmployees(ActionEvent event) throws IOException {
        switchScene(event, "Employee.fxml");
    }

    @FXML
    private void handleLeaveRequests(ActionEvent event) throws IOException {
        switchScene(event, "LeaveRequest.fxml");
    }

    @FXML
    private void handleOTRequests(ActionEvent event) throws IOException {
        switchScene(event, "OTRequest.fxml");
    }

    @FXML
    private void handleTimeStamps(ActionEvent event) throws IOException {
        switchScene(event, "TimeStamp.fxml");
    }

    @FXML
    private void handlePayslip(ActionEvent event) throws IOException {
        switchScene(event, "Payslip.fxml");
    }

    @FXML
    private void handleEdit(ActionEvent event) throws IOException {
        switchScene(event, "EmployeeEDIT.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error loading Login.fxml.");
        }
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
