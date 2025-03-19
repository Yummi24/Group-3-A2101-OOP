package com.example.motorphoop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AttendanceDetailsController {

    @FXML private Label lblEmployeeID;
    @FXML private Label lblEmployeeName;
    @FXML private Label lblDate;
    @FXML private Label lblLogIn;
    @FXML private Label lblLogOut;
    @FXML private Label lblTotalHours;

    // Display Attendance Details
    public void displayAttendanceDetails(String id, String name, String date, String logIn, String logOut, String totalHours) {
        lblEmployeeID.setText(id);
        lblEmployeeName.setText(name);
        lblDate.setText(date);
        lblLogIn.setText(logIn);
        lblLogOut.setText(logOut);
        lblTotalHours.setText(totalHours);
    }
}
