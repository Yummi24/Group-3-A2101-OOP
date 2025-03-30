package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PayslipController implements Initializable {

    @FXML private Text idLabel, nameLabel, positionLabel;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private TextArea payslipTextArea;
    @FXML private Button editButton, saveButton;

    private String employeeId;
    private String employeeName;
    private String employeePosition;
    private String supervisor;
    private String phoneNumber;
    private double hourlyRate;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossSemiMonthlyRate;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private static final String CSV_FILE = "src/Employees.csv";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateMonthComboBox();
        payslipTextArea.setEditable(false);
        saveButton.setDisable(true);
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

        fetchEmployeeDetails();
    }

    private void fetchEmployeeDetails() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 13 && data[0].equals(employeeId)) {
                    supervisor = data[7];
                    phoneNumber = data[5];
                    hourlyRate = Double.parseDouble(data[14]);
                    basicSalary = Double.parseDouble(data[13]);


                    riceSubsidy = (data.length > 16) ? Double.parseDouble(data[16]) : 0.0;
                    phoneAllowance = (data.length > 17) ? Double.parseDouble(data[17]) : 0.0;
                    clothingAllowance = (data.length > 18) ? Double.parseDouble(data[18]) : 0.0;
                    grossSemiMonthlyRate = (data.length > 19) ? Double.parseDouble(data[19]) : 0.0;


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
        }
    }

    @FXML
    private void generatePayslip() {
        String selectedMonth = monthComboBox.getValue();
        if (selectedMonth == null) return;

        int totalDaysWorked = calculateWeekdays(selectedMonth);
        double totalHoursWorked = totalDaysWorked * 8;
        double overtimeHours = Math.random() * 20;
        double overtimePay = overtimeHours * (hourlyRate * 1.25);
        double deductions = basicSalary * 0.1;
        double netPay = basicSalary + riceSubsidy + phoneAllowance + clothingAllowance - deductions + overtimePay;

        payslipTextArea.setText(
                "========== PAYSLIP ==========" +
                        "\nEmployee ID      : " + employeeId +
                        "\nName             : " + employeeName +
                        "\nPosition         : " + employeePosition +
                        "\nSupervisor       : " + supervisor +
                        "\nPhone Number     : " + phoneNumber +
                        "\nMonth           : " + selectedMonth +
                        "\n-------------------------------" +
                        "\nTotal Days Worked : " + totalDaysWorked +
                        "\nTotal Hours Worked: " + df.format(totalHoursWorked) +
                        "\n-------------------------------" +
                        "\nBasic Salary      : \u20B1 " + df.format(basicSalary) +
                        "\nRice Subsidy      : \u20B1 " + df.format(riceSubsidy) +
                        "\nPhone Allowance   : \u20B1 " + df.format(phoneAllowance) +
                        "\nClothing Allowance: \u20B1 " + df.format(clothingAllowance) +
                        "\nGross Semi-Monthly: \u20B1 " + df.format(grossSemiMonthlyRate) +
                        "\nDeductions        : \u20B1 " + df.format(deductions) +
                        "\nOvertime          : \u20B1 " + df.format(overtimePay) +
                        "\n-------------------------------" +
                        "\nNET PAY           : \u20B1 " + df.format(netPay) +
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
    private void handleEditPayslip() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Edit");
        confirmationAlert.setHeaderText("Are you sure you want to edit this employee's payslip?");
        confirmationAlert.setContentText("Changes will be saved to the CSV file.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            payslipTextArea.setEditable(true);
            saveButton.setDisable(false);
        }
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
