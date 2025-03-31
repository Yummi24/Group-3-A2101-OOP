package com.example.motorphoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private VBox employeeListContainer;
    @FXML private TextField searchField;

    private List<Employee> employees = new ArrayList<>();
    private String employeeID;

    @FXML
    public void initialize() {
        loadEmployeeData();
    }

    private static class Employee {
        private final String id;
        private final String lastName;
        private final String firstName;
        private final String birthday;
        private final String address;
        private final String phone;
        private final String sss;
        private final String philhealth;
        private final String tin;
        private final String pagibig;
        private final String position;
        private final String supervisor;

        public Employee(String[] data) {
            this.id = data[0].trim();
            this.lastName = data[1].trim();
            this.firstName = data[2].trim();
            this.birthday = data[3].trim();
            this.address = data[4].trim();
            this.phone = data[5].trim();
            this.sss = data[6].trim();
            this.philhealth = data[7].trim();
            this.tin = data[8].trim();
            this.pagibig = data[9].trim();
            this.position = data[10].trim();
            this.supervisor = data[11].trim();
        }

        public String getId() { return id; }
        public String getFullName() { return firstName + " " + lastName; }
        public String getPosition() { return position; }
        public String getBirthday() { return birthday; }
        public String getAddress() { return address; }
        public String getPhone() { return phone; }
        public String getSss() { return sss; }
        public String getPhilhealth() { return philhealth; }
        public String getTin() { return tin; }
        public String getPagibig() { return pagibig; }
        public String getSupervisor() { return supervisor; }
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    private void loadEmployeeData() {
        employees.clear();
        employeeListContainer.getChildren().clear();

        try (BufferedReader br = new BufferedReader(new FileReader("src/Employees.csv"))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] details = line.split(",");
                if (details.length >= 12) {
                    Employee employee = new Employee(details);
                    employees.add(employee);
                    employeeListContainer.getChildren().add(createEmployeeEntry(employee));
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading employee data: " + e.getMessage());
        }
    }

    private HBox createEmployeeEntry(Employee employee) {
        HBox box = new HBox(10);
        box.setStyle("-fx-border-color: #1f4a8d; -fx-border-width: 1; -fx-padding: 5;");

        Label idLabel = createLabel(employee.getId(), 80);
        Label nameLabel = createLabel(employee.getFullName(), 200);
        Label positionLabel = createLabel(employee.getPosition(), 200);

        box.getChildren().addAll(idLabel, nameLabel, positionLabel);
        box.setOnMouseClicked(event -> showEmployeeDetails(employee));

        return box;
    }

    private void showEmployeeDetails(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeDetails.fxml"));
            Parent root = loader.load();

            EmployeeDetailsController detailsController = loader.getController();
            detailsController.displayEmployeeDetails(
                    employee.getId(),
                    employee.getFullName(),
                    employee.getBirthday(),
                    employee.getAddress(),
                    employee.getPhone(),
                    employee.getSss(),
                    employee.getPhilhealth(),
                    employee.getTin(),
                    employee.getPagibig(),
                    employee.getPosition(),
                    employee.getSupervisor()
            );

            Stage stage = (Stage) employeeListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading EmployeeDetails.fxml: " + e.getMessage());
        }
    }

    private Label createLabel(String text, int minWidth) {
        Label label = new Label(text);
        label.setMinWidth(minWidth);
        return label;
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim().toLowerCase();
        employeeListContainer.getChildren().clear();

        for (Employee employee : employees) {
            if (employee.getId().toLowerCase().contains(searchText) ||
                    employee.getFullName().toLowerCase().contains(searchText)) {
                employeeListContainer.getChildren().add(createEmployeeEntry(employee));
            }
        }
    }

    @FXML
    private void handleAddEdit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Edit.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading " + fxmlFile + ": " + e.getMessage());
        }
    }
}
