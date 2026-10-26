package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private final Connection connection;
    private final Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.print("Enter Patient name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Patient age: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid age!");
            return;
        }
        System.out.print("Enter Patient gender: ");
        String gender = scanner.nextLine().trim();

        if (name.isEmpty() || gender.isEmpty() || age <= 0) {
            System.out.println("Invalid input. Patient not added!");
            return;
        }

        String query = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            if (ps.executeUpdate() > 0) {
                System.out.println("Patient added successfully!");
            } else {
                System.out.println("Failed to add patient!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = "SELECT * FROM patients";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            String line = "+------------+---------------+-------+---------+";
            System.out.println(line);
            System.out.println("| Patient Id | Name          | Age   | Gender  |");
            System.out.println(line);
            while (rs.next()) {
                System.out.printf("|%-12d|%-15s|%-7d|%-9s|%n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getInt("age"), rs.getString("gender"));
                System.out.println(line);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT 1 FROM patients WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
