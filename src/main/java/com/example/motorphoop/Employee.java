package com.example.motorphoop;

public class Employee {
    private String employeeID, lastName, firstName, birthday, address, phoneNumber;
    private String sss, philHealth, tin, pagIbig, status, position, immediateSupervisor;
    private double basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate;
    private String Username, Password, Role;

    public Employee(String[] data) {
        this.employeeID = data[0].trim();
        this.lastName = data[1].trim();
        this.firstName = data[2].trim();
        this.birthday = data[3].trim();
        this.address = data[4].trim();
        this.phoneNumber = data[5].trim();
        this.sss = data[6].trim();
        this.philHealth = data[7].trim();
        this.tin = data[8].trim();
        this.pagIbig = data[9].trim();
        this.status = data[10].trim();
        this.position = data[11].trim();
        this.immediateSupervisor = data[12].trim();

        // Convert numeric values
        this.basicSalary = parseDouble(data[13]);
        this.riceSubsidy = parseDouble(data[14]);
        this.phoneAllowance = parseDouble(data[15]);
        this.clothingAllowance = parseDouble(data[16]);
        this.grossSemiMonthlyRate = parseDouble(data[17]);
        this.hourlyRate = parseDouble(data[18]);

        this.Username = data[19].trim();
        this.Password = data[20].trim();
        this.Role = data[21].trim();
    }

    // Helper method to handle parsing
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim().replace(",", "")); // Remove commas in case of formatted numbers
        } catch (NumberFormatException e) {
            return 0.0; // Default to 0 if parsing fails
        }
    }

    // Getters
    public String getEmployeeID() { return employeeID; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSSS() { return sss; }
    public String getPhilHealth() { return philHealth; }
    public String getTIN() { return tin; }
    public String getPagIbig() { return pagIbig; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getImmediateSupervisor() { return immediateSupervisor; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public double getHourlyRate() { return hourlyRate; }
    public String getUsername() { return Username; }
    public String getPassword() { return Password; }
    public String getRole() { return Role; }
}
