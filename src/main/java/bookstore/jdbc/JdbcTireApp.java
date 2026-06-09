package bookstore.jdbc;

import bookstore.pojos.Tire;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class JdbcTireApp {
    // Database connection infrastructure configurations
    private static final String DB_URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "root";
    private static final String PASS = "itstudies12345"; // Using the default template password matching the instructor's setup

    public static void main(String[] args) {
        JdbcTireApp app = new JdbcTireApp();
        app.createTable();
    }

    /**
     * Phase 1: DDL Schema Design
     * Establishes the 'Tires' table applying custom DBA naming conventions and optimized data types.
     */
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Tires ("
                + "tireID INT AUTO_INCREMENT PRIMARY KEY, "
                + "tireProductID VARCHAR(36) NOT NULL, " // Formatted to persist Java's UUID string mapping
                + "tireManufacturer VARCHAR(30) NOT NULL, " // Optimized length based on production domain analysis
                + "tirePrice DOUBLE NOT NULL, "
                + "tireDiameter INT NOT NULL, "
                + "tireModel VARCHAR(50), "
                + "tireSize VARCHAR(20), "
                + "tireWidth INT, "
                + "tireRimSize INT, "
                + "tireSeasonType VARCHAR(20), "
                + "tireCreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Database Schema: 'Tires' table successfully verified or created.");

        } catch (SQLException e) {
            System.err.println("DDL Execution Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2: Secure Data Insertion
     * Persists a domain Tire instance using a Parameterized PreparedStatement to block SQL Injection risks.
     */
    public void insertItem(Tire i) {
        String sql = "INSERT INTO Tires (tireProductID, tireManufacturer, tirePrice, tireDiameter, "
                + "tireModel, tireSize, tireWidth, tireRimSize, tireSeasonType) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Mapping the native domain class identity and core attributes
            pstmt.setString(1, i.getProductId());
            pstmt.setString(2, i.getManufacturer());
            pstmt.setDouble(3, i.getPrice());
            pstmt.setInt(4, i.getDiameter());

            // Binding the new specialized technical operational fields
            pstmt.setString(5, "Potenza");
            pstmt.setString(6, "225/45R18");
            pstmt.setInt(7, 225);
            pstmt.setInt(8, 18);
            pstmt.setString(9, "Summer");

            pstmt.executeUpdate();
            System.out.println("Data Persistence: Tire record successfully saved to the backend repository.");

        } catch (SQLException e) {
            System.err.println("DML Execution Failure: " + e.getMessage());
        }
    }


    /**
     * Phase 2 & 3: Read Operation (Object Rehydration)
     * Executes a SELECT query to retrieve all database rows and rehydrates them into Java Tire instances[cite: 34, 42].
     */
    public void listItems() {
        String sql = "SELECT tireProductID, tireManufacturer, tirePrice, tireDiameter FROM Tires";

        System.out.println("\n--- Fetching Persistent Records from 'Tires' Table ---");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Extract relational data from columns
                String productId = rs.getString("tireProductID");
                String manufacturer = rs.getString("tireManufacturer");
                double price = rs.getDouble("tirePrice");
                int diameter = rs.getInt("tireDiameter");

                // Rehydrating the domain object utilizing the correct 3-argument constructor
                Tire tire = new Tire(manufacturer, price, diameter);

                // Phase 3 Identity Mapping: Restoring the unique universal identifier (UUID) [cite: 42]
                tire.setProductId(productId);

                // Printing the structural state of the domain object
                System.out.println("Hydrated Object -> ID: " + tire.getProductId()
                        + " | Brand: " + tire.getManufacturer()
                        + " | Price: $" + tire.getPrice()
                        + " | Diameter: " + tire.getDiameter() + " inches");
            }
        } catch (SQLException e) {
            System.err.println("Read Operation Failure: " + e.getMessage());
        }
    }


    /**
     * Phase 2 & 3: Update Operation
     * Safely updates a persistent tire price matched by its unique UUID index or manufacturer string.
     */
    public void updatePrice(String manufacturer, double newPrice) {
        String sql = "UPDATE Tires SET tirePrice = ? WHERE tireManufacturer = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, manufacturer);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Update Operation: " + rowsAffected + " row(s) updated successfully.");

        } catch (SQLException e) {
            System.err.println("Update Operation Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Delete Operation
     * Safely drops a persistent record using parameterized constraints to isolate execution target.
     */
    public void deleteItem(String manufacturer) {
        String sql = "DELETE FROM Tires WHERE tireManufacturer = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, manufacturer);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Delete Operation: " + rowsAffected + " row(s) removed from backend storage.");

        } catch (SQLException e) {
            System.err.println("Delete Operation Failure: " + e.getMessage());
        }
    }
}