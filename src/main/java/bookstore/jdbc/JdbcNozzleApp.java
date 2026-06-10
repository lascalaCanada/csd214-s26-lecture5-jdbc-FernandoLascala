package bookstore.jdbc;

import bookstore.pojos.Nozzle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class JdbcNozzleApp {

    // Database connection infrastructure configurations
    private final Jdbc dbConfig = new Jdbc();


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

        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Database Schema: 'Filaments' table successfully verified or created.",
                    "DDL Execution Failure: ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Phase 2 & 3: Secure Data Insertion (Outcome 6.4 Compliant)
     */
    public void insertItem(Nozzle i) {
        String sql = "INSERT INTO Nozzles (nozzleProductID, nozzleBrand, nozzleDiameter, nozzlePrice, nozzleCopies) "
                + "VALUES (?, ?, ?, ?, ?)";


        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Data Persistence: Nozzle record successfully saved to the backend repository.",
                    "DML Execution Failure: ",
                    i.getProductId(),
                    i.getBrand(),
                    i.getDiameter(),
                    i.getPrice(),
                    i.getCopies());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Phase 2 & 3: Read Operation & Identity Preservation (Outcome 6.4 Compliant)
     */
    public void listItems() {
        String sql = "SELECT nozzleProductID, nozzleBrand, nozzleDiameter, nozzlePrice, nozzleCopies FROM Nozzles";

        try (Connection conn = DriverManager.getConnection(dbConfig.url(), dbConfig.user(), dbConfig.pass());
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String productId = rs.getString("nozzleProductID");
                String brand = rs.getString("nozzleBrand");
                double diameter = rs.getDouble("nozzleDiameter");
                double price = rs.getDouble("nozzlePrice");
                int copies = rs.getInt("nozzleCopies");

                Nozzle nozzle = new Nozzle(brand, price, copies, diameter);

                // Rule compliance: Rehydrating the entity lifecycle state with its original database identity
                nozzle.setProductId(productId);

                System.out.println("Hydrated Object -> ID: " + nozzle.getProductId()
                        + " | Brand: " + nozzle.getBrand()
                        + " | Diameter: " + nozzle.getDiameter() + "mm");
            }
        } catch (SQLException e) {
            System.err.println("Read Operation Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Safe Update Operation by Product UUID
     * Ensures only the exact targeted nozzle record is modified.
     */
    // Modified parameter type to int to keep strict typing with the primary database identifier index
    public void updatePrice(int nozzleId, double newPrice) {
        String sql = "UPDATE Nozzles SET nozzlePrice = ? WHERE nozzleId = ?";

        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Update Operation Successfully.",
                    "DML Execution Failure: ",
                    newPrice,
                    nozzleId); // Passed as an integer parameter to target the matching PK row
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Phase 2 & 3: Safe Delete Operation by Product UUID
     * Restricts the removal to a single unique record, preventing accidental data loss.
     */
    // Modified parameter type to int to map accurately to the relational primary key schema
    public void deleteItem(int nozzleId) {
        String sql = "DELETE FROM Nozzles WHERE nozzleId = ?";

        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Delete Operation from backend storage successfully.",
                    "DML Execution Failure: ",
                    nozzleId); // Passed as an integer parameter to target the matching PK row
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Executes database operations dynamically, handling both direct Statements (DDL)
     * and secure Parameterized PreparedStatements (DML) based on arguments.
     */
    private void databaseExecs(String sql, String successMessage, String errorMessage, Object... parameters) throws SQLException {

        // Connection setup using dbConfig.url(), dbConfig.user(), and dbConfig.password() from your record
        try (Connection conn = DriverManager.getConnection(dbConfig.url(), dbConfig.user(), dbConfig.pass());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Phase 3 Anti-Injection: Iterates and binds query variables if present
            for (int i = 0; i < parameters.length; i++) {
                Object param = parameters[i];
                int parameterIndex = i + 1;

                // Evaluation about which the parameter is
                if (param instanceof String) {
                    pstmt.setString(parameterIndex, (String) param);
                } else if (param instanceof Double) {
                    pstmt.setDouble(parameterIndex, (Double) param);
                } else if (param instanceof Integer) {
                    pstmt.setInt(parameterIndex, (Integer) param);
                } else if (param == null) {
                    pstmt.setNull(parameterIndex, java.sql.Types.NULL);
                }
            }

            // Executes the statement safely
            pstmt.execute();
            System.out.println(successMessage);

        } catch (SQLException e) {
            System.err.println(errorMessage + e.getMessage());
        }
    }
}