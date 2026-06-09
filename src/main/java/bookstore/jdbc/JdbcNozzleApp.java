package bookstore.jdbc;

import bookstore.pojos.Nozzle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class JdbcNozzleApp {
    // Database connection infrastructure configurations
    private static final String DB_URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "root";
    private static final String PASS = "itstudies12345";

    public static void main(String[] args) {
        JdbcNozzleApp app = new JdbcNozzleApp();
        app.createTable();
    }

    /**
     * Phase 1: DDL Schema Design
     * Establishes the 'Nozzles' table applying custom naming conventions and optimized data types.
     */
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Nozzles ("
                + "nozzleID INT AUTO_INCREMENT PRIMARY KEY, "
                + "nozzleProductID VARCHAR(36) NOT NULL, "              // Formatted to persist Java's UUID string mapping
                + "nozzleBrand VARCHAR(30) NOT NULL, "                  // Mapped from getBrand()
                + "nozzleDiameter DOUBLE, "                             // Mapped from getDiameter()
                + "nozzlePrice DOUBLE NOT NULL DEFAULT(0), "            // Mapped from getPrice()
                + "nozzleCopies INT NOT NULL DEFAULT(0), "              // Mapped from getCopies()
                + "nozzleCreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Database Schema: 'Nozzles' table successfully verified or created.");

        } catch (SQLException e) {
            System.err.println("DDL Execution Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Secure Data Insertion
     * Persists a domain Nozzle instance using a Parameterized PreparedStatement to block SQL Injection risks.
     */
    public void insertItem(Nozzle i) {
        String sql = "INSERT INTO Nozzles (nozzleProductID, nozzleBrand, nozzleDiameter, nozzlePrice, nozzleCopies) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Phase 3 Identity Mapping: Extracting the domain object's UUID string
            pstmt.setString(1, i.getProductId());
            pstmt.setString(2, i.getBrand());
            pstmt.setDouble(3, i.getDiameter());
            pstmt.setDouble(4, i.getPrice());
            pstmt.setInt(5, i.getCopies());

            pstmt.executeUpdate();
            System.out.println("Data Persistence: Nozzle record successfully saved to the backend repository.");

        } catch (SQLException e) {
            System.err.println("DML Execution Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Read Operation (Object Rehydration)
     * Executes a SELECT query to retrieve all database rows and rehydrates them into Java Nozzle instances.
     */
    public void listItems() {
        String sql = "SELECT nozzleProductID, nozzleBrand, nozzleDiameter, nozzlePrice, nozzleCopies FROM Nozzles";

        System.out.println("\n--- Fetching Persistent Records from 'Nozzles' Table ---");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Extract relational data from columns
                String productId = rs.getString("nozzleProductID");
                String brand = rs.getString("nozzleBrand");
                double diameter = rs.getDouble("nozzleDiameter");
                double price = rs.getDouble("nozzlePrice");
                int copies = rs.getInt("nozzleCopies");

                // Rehydrating utilizing your exact alternate constructor: Nozzle(genericManufacturer, price, stock, diameter)
                Nozzle nozzle = new Nozzle(brand, price, copies, diameter);

                // Phase 3 Identity Mapping: Restoring the unique universal identifier (UUID)
                nozzle.setProductId(productId);

                System.out.println("Hydrated Object -> ID: " + nozzle.getProductId()
                        + " | Brand: " + nozzle.getBrand()
                        + " | Diameter: " + nozzle.getDiameter() + "mm"
                        + " | Price: $" + nozzle.getPrice()
                        + " | Stock: " + nozzle.getCopies());
            }
        } catch (SQLException e) {
            System.err.println("Read Operation Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Safe Update Operation by Product UUID
     * Ensures only the exact targeted nozzle record is modified.
     */
    public void updatePrice(String productId, double newPrice) {
        String sql = "UPDATE Nozzles SET nozzlePrice = ? WHERE nozzleProductID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, productId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Update Operation: " + rowsAffected + " row(s) updated successfully.");

        } catch (SQLException e) {
            System.err.println("Update Operation Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Safe Delete Operation by Product UUID
     * Restricts the removal to a single unique record, preventing accidental data loss.
     */
    public void deleteItem(String productId) {
        String sql = "DELETE FROM Nozzles WHERE nozzleProductID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Delete Operation: " + rowsAffected + " row(s) removed from backend storage.");

        } catch (SQLException e) {
            System.err.println("Delete Operation Failure: " + e.getMessage());
        }
    }
}