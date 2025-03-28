package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PayslipController implements Initializable {

    @FXML private Text idLabel;
    @FXML private Text nameLabel;
    @FXML private Text positionLabel;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private TextArea payslipTextArea;

    private String employeeId;
    private String employeeName;
    private String employeePosition;
    private double basicSalary;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateMonthComboBox();
    }

    private void populateMonthComboBox() {
        monthComboBox.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
    }
    public void setEmployeeData(String id, String name, String position) {
        this.employeeId = id;
        this.employeeName = name;
        this.employeePosition = position;

        idLabel.setText(id);
        nameLabel.setText(name);
        positionLabel.setText(position);

        fetchEmployeeSalary();
    }

    private void fetchEmployeeSalary() {
        String csvFile = "src/Employees.csv";
        String line;
        String splitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(splitBy);
                if (data.length > 13 && data[0].equals(employeeId)) {
                    basicSalary = Double.parseDouble(data[13]); // Column 13: Basic Salary
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void generatePayslip() {
        String selectedMonth = monthComboBox.getValue();
        if (selectedMonth == null) return;

        int totalDaysWorked = calculateWeekdays(selectedMonth);
        double totalHoursWorked = totalDaysWorked * 8;
        double overtimeHours = Math.random() * 20; // Random OT between 0-20 hours
        double overtimePay = overtimeHours * (basicSalary / 160 * 1.25);
        double deductions = basicSalary * 0.1; // 10% tax/deductions
        double netPay = basicSalary - deductions + overtimePay;


        payslipTextArea.setText(
                "========== PAYSLIP ==========" +
                        "\nEmployee ID  : " + employeeId +
                        "\nName         : " + employeeName +
                        "\nPosition     : " + employeePosition +
                        "\nMonth       : " + selectedMonth +
                        "\n-------------------------------" +
                        "\nTotal Days Worked : " + totalDaysWorked +
                        "\nTotal Hours Worked: " + df.format(totalHoursWorked) +
                        "\n-------------------------------" +
                        "\nBasic Salary : \u20B1 " + df.format(basicSalary) +
                        "\nDeductions   : \u20B1 " + df.format(deductions) +
                        "\nOvertime     : \u20B1 " + df.format(overtimePay) +
                        "\n-------------------------------" +
                        "\nNET PAY      : \u20B1 " + df.format(netPay) +
                        "\n================================"
        );
    }

    private int calculateWeekdays(String month) {
        int year = LocalDate.now().getYear();
        int monthValue = switch (month) {
            case "January" -> 1;
            case "February" -> 2;
            case "March" -> 3;
            case "April" -> 4;
            case "May" -> 5;
            case "June" -> 6;
            case "July" -> 7;
            case "August" -> 8;
            case "September" -> 9;
            case "October" -> 10;
            case "November" -> 11;
            case "December" -> 12;
            default -> 1;
        };

        LocalDate start = LocalDate.of(year, monthValue, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        int weekdays = 0;

        while (!start.isAfter(end)) {
            if (start.getDayOfWeek() != DayOfWeek.SATURDAY && start.getDayOfWeek() != DayOfWeek.SUNDAY) {
                weekdays++;
            }
            start = start.plusDays(1);
        }
        return weekdays;
    }

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payslip.fxml"));
        Parent root = loader.load();
        PayslipController controller = loader.getController();
        controller.setEmployeeData(employeeId, employeeName, employeePosition);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            switchScene(event, "Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Login.fxml. Check the file path.");
        }
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void receiveEmployeeDetails(String employeeId, String name, String position) {
        setEmployeeData(employeeId, name, position);
    }
}
