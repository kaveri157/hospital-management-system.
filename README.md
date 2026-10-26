# Hospital Management System

A console-based Hospital Management System built with **Java (JDBC)** and **MySQL**, developed as a DBMS mini project (TE, AI & Data Science, SPPU).

## Features
- Add and view patients
- View doctors
- Book appointments (validates patient/doctor and prevents double-booking a doctor on the same date)

## Tech Stack
Java 17+, MySQL 8, JDBC (MySQL Connector/J), Maven

## Project Structure
```
hospital-management-system/
├── pom.xml
├── schema.sql
└── src/main/java/HospitalManagementSystem/
    ├── HospitalManagementSystem.java   # main menu + appointment booking
    ├── Patient.java
    └── Doctor.java
```

## Setup
1. Install JDK 17+, Maven and MySQL.
2. Create the database and tables:
   ```
   mysql -u root -p < schema.sql
   ```
3. Set your database credentials as environment variables (nothing is hard-coded):
   - Windows (PowerShell): `$env:DB_PASSWORD="your_password"`
   - Linux/macOS: `export DB_PASSWORD=your_password`

   Optional: `DB_USER` (default `root`), `DB_URL` (default `jdbc:mysql://localhost:3306/hospital?...`).
4. Run:
   ```
   mvn compile exec:java
   ```

## Sample Menu
```
HOSPITAL MANAGEMENT SYSTEM
1. Add Patient
2. View Patients
3. View Doctors
4. Book Appointments
5. Exit
```

## Future Scope
Login for admin/doctor/receptionist, room & bed allocation, billing, online booking, digital medical records.

## Author
Kaveri Dhatrak — PVG's COE & SSDIOM, Nashik (SPPU)
