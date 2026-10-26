package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {

    // Credentials come from environment variables so no password is committed to GitHub.
    private static final String URL = System.getenv().getOrDefault(
            "DB_URL", "jdbc:mysql://localhost:3306/hospital?useSSL=false&allowPublicKeyRetrieval=true");
    private static final String USERNAME = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointments");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Enter valid choice!\n");
                    continue;
                }

                switch (choice) {
                    case 1 -> patient.addPatient();
                    case 2 -> patient.viewPatients();
                    case 3 -> doctor.viewDoctors();
                    case 4 -> bookAppointment(patient, doctor, connection, scanner);
                    case 5 -> {
                        System.out.println("Exiting program...");
                        return;
                    }
                    default -> System.out.println("Enter valid choice!");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to the database. Check DB_URL / DB_USER / DB_PASSWORD.");
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor,
                                       Connection connection, Scanner scanner) {
        int patientId, doctorId;
        try {
            System.out.print("Enter patient id: ");
            patientId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter doctor id: ");
            doctorId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ids must be numbers!");
            return;
        }

        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        java.sql.Date appointmentDate;
        try {
            appointmentDate = java.sql.Date.valueOf(scanner.nextLine().trim());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format! Use YYYY-MM-DD.");
            return;
        }

        if (!patient.getPatientById(patientId) || !doctor.getDoctorById(doctorId)) {
            System.out.println("Either doctor or patient doesn't exist!!");
            return;
        }

        if (!checkDoctorAvailability(doctorId, appointmentDate, connection)) {
            System.out.println("Doctor is not available!");
            return;
        }

        String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setDate(3, appointmentDate);
            if (ps.executeUpdate() > 0) {
                System.out.println("Appointment booked successfully!");
            } else {
                System.out.println("Failed to book appointment!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, java.sql.Date appointmentDate,
                                                  Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, appointmentDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
