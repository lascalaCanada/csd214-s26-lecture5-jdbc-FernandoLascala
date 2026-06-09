package bookstore.jdbc;

import bookstore.pojos.Filament;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class JdbcFilamentApp {
    // Database connection infrastructure configurations
    private static final String DB_URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "root";
    private static final String PASS = "itstudies12345";

    public static void main(String[] args) {
        JdbcFilamentApp app = new JdbcFilamentApp();
        app.createTable();
    }

    /**
     * Phase 1: DDL Schema Design
     * Establishes the 'Filaments' table applying custom DBA naming conventions and optimized data types.
     */
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Filaments ("
                + "filamentID INT AUTO_INCREMENT PRIMARY KEY, "
                + "filamentProductID VARCHAR(36) NOT NULL, " // Formatted to persist Java's UUID string mapping
                + "filamentBrand VARCHAR(30) NOT NULL, "     // Mapped from getBrand()
                + "filamentMaterial VARCHAR(30) NOT NULL, "  // Mapped from getMaterialType() (PLA, ABS, etc.)
                + "filamentPrice DOUBLE NOT NULL, "          // Mapped from getPrice()
                + "filamentCopies INT NOT NULL, "            // Mapped from getCopies()
                + "filamentCreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Database Schema: 'Filaments' table successfully verified or created.");

        } catch (SQLException e) {
            System.err.println("DDL Execution Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Secure Data Insertion
     * Persists a domain Filament instance using a Parameterized PreparedStatement to block SQL Injection risks.
     */
    public void insertItem(Filament i) {
        String sql = "INSERT INTO Filaments (filamentProductID, filamentBrand, filamentMaterial, filamentPrice, filamentCopies) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Phase 3 Identity Mapping: Extracting the domain object's UUID string
            pstmt.setString(1, i.getProductId());
            pstmt.setString(2, i.getBrand());
            pstmt.setString(3, i.getMaterialType());
            pstmt.setDouble(4, i.getPrice());
            pstmt.setInt(5, i.getCopies());

            pstmt.executeUpdate();
            System.out.println("Data Persistence: Filament record successfully saved to the backend repository.");

        } catch (SQLException e) {
            System.err.println("DML Execution Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Read Operation (Object Rehydration)
     * Executes a SELECT query to retrieve all database rows and rehydrates them into Java Filament instances.
     */
    public void listItems() {
        String sql = "SELECT filamentProductID, filamentBrand, filamentMaterial, filamentPrice, filamentCopies FROM Filaments";

        System.out.println("\n--- Fetching Persistent Records from 'Filaments' Table ---");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Extract relational data from columns
                String productId = rs.getString("filamentProductID");
                String brand = rs.getString("filamentBrand");
                String material = rs.getString("filamentMaterial");
                double price = rs.getDouble("filamentPrice");
                int copies = rs.getInt("filamentCopies");

                // Rehydrating utilizing your exact alternate constructor: Filament(genericManufacturer, price, copies, materialType)
                Filament filament = new Filament(brand, price, copies, material);

                // Phase 3 Identity Mapping: Restoring the unique universal identifier (UUID)
                filament.setProductId(productId);

                System.out.println("Hydrated Object -> ID: " + filament.getProductId()
                        + " | Brand: " + filament.getBrand()
                        + " | Material: " + filament.getMaterialType()
                        + " | Price: $" + filament.getPrice()
                        + " | Stock: " + filament.getCopies());
            }
        } catch (SQLException e) {
            System.err.println("Read Operation Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Safe Update Operation by Product UUID
     * Ensures only the exact targeted filament record is modified.
     */
    public void updatePrice(String productId, double newPrice) {
        String sql = "UPDATE Filaments SET filamentPrice = ? WHERE filamentProductID = ?";

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
        String sql = "DELETE FROM Filaments WHERE filamentProductID = ?";

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