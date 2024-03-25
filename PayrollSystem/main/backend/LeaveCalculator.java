package main.java.backend;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LeaveCalculator {

    private String employeeID, reason, firstName, lastName;
    private int addLeaveUse, reduceLeaveRemaining, add_TTLLeaveUse, reduce_TTLLeaveRemaining;
    private final String recordPath = "src/resources/data/Leave.csv";
    private final String historyPath = "src/resources/data/LeaveHistory.csv";

    public LeaveCalculator(String employeeID, String reason) {
        this.employeeID = employeeID;
        this.reason = reason;
    }

    public void calculateLeave(int leaveUse, int leaveRemaining, int total_LeaveUse, int total_LeaveRemaining) {
        addLeaveUse = leaveUse + 1;
        reduceLeaveRemaining = leaveRemaining - 1;
        add_TTLLeaveUse = total_LeaveUse + 1;
        reduce_TTLLeaveRemaining = total_LeaveRemaining - 1;
        updateLeave();
    }

    private void updateLeave() {
        try {
            // Open the CSV file and create a reader
            CSVReader reader = new CSVReader(new FileReader(recordPath));
            List<String[]> csvData = reader.readAll();
            reader.close();

            // Find the row for the given user ID
            int empNumIndex = -1;
            for (int i = 0; i < csvData.get(0).length; i++) {
                if (csvData.get(0)[i].equals("Employee #")) {
                    empNumIndex = i;
                    break;
                }
            }
            if (empNumIndex == -1) {
                System.out.println("Error: Could not find 'Employee #' column in CSV file");
                return;
            }

            boolean foundUser = false;
            for (String[] row : csvData) {
                if (row[empNumIndex].equals(employeeID)) {
                    switch (reason) {
                        case "Sick" -> {
                            row[3] = Integer.toString(addLeaveUse);
                            row[6] = Integer.toString(reduceLeaveRemaining);
                            row[9] = Integer.toString(add_TTLLeaveUse);
                            row[10] = Integer.toString(reduce_TTLLeaveRemaining);
                        }
                        case "Vacation" -> {
                            row[4] = Integer.toString(addLeaveUse);
                            row[7] = Integer.toString(reduceLeaveRemaining);
                            row[9] = Integer.toString(add_TTLLeaveUse);
                            row[10] = Integer.toString(reduce_TTLLeaveRemaining);
                        }
                        case "Emergency" -> {
                            row[5] = Integer.toString(addLeaveUse);
                            row[8] = Integer.toString(reduceLeaveRemaining);
                            row[9] = Integer.toString(add_TTLLeaveUse);
                            row[10] = Integer.toString(reduce_TTLLeaveRemaining);
                        }
                    }
                    foundUser = true;
                    break;
                }
            }

            if (!foundUser) {
                System.out.println("Error: Could not find user with ID " + employeeID + " in CSV file");
                return;
            }

            // Write the updated data back to the CSV file
            CSVWriter writer = new CSVWriter(new FileWriter(recordPath));
            writer.writeAll(csvData);
            writer.close();
        } catch (IOException | CsvException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
