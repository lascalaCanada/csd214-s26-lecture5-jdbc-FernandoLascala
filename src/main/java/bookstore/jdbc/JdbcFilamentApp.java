package bookstore.jdbc;

import bookstore.pojos.Filament;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcFilamentApp {

    // Database connection infrastructure configurations
    private final Jdbc dbConfig = new Jdbc();

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
     * Uses Parameterized PreparedStatement to block SQL Injection and maps the domain UUID string.
     */
    public void insertItem(Filament i) {
        // Strict parameterization using '?' to eliminate string concatenation risks
        String sql = "INSERT INTO Filaments (filamentProductID, filamentBrand, filamentMaterial, filamentPrice, filamentCopies) "
                + "VALUES (?, ?, ?, ?, ?)";

        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Data Persistence: Filament record successfully saved to the backend repository.",
                    "DML Execution Failure: ",
                    i.getProductId(),
                    i.getBrand(),
                    i.getMaterialType(),
                    i.getPrice(),
                    i.getCopies());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Phase 2 & 3: Read Operation & Identity Preservation (Outcome 6.4 Compliant)
     * Pulls the unique UUID string back and rehydrates the object identity.
     */
    public void listItems() {
        String sql = "SELECT filamentProductID, filamentBrand, filamentMaterial, filamentPrice, filamentCopies FROM Filaments";

        try (Connection conn = DriverManager.getConnection(dbConfig.url(), dbConfig.user(), dbConfig.pass());
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Pulling the persistent identity string back from the database column
                String productId = rs.getString("filamentProductID");
                String brand = rs.getString("filamentBrand");
                String material = rs.getString("filamentMaterial");
                double price = rs.getDouble("filamentPrice");
                int copies = rs.getInt("filamentCopies");

                Filament filament = new Filament(brand, price, copies, material);

                // Rule compliance: Restoring the exact unique universal identifier to maintain object identity
                filament.setProductId(productId);

                System.out.println("Hydrated Object -> ID: " + filament.getProductId()
                        + " | Brand: " + filament.getBrand()
                        + " | Material: " + filament.getMaterialType());
            }
        } catch (SQLException e) {
            System.err.println("Read Operation Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Safe Update Operation by Product UUID
     * Ensures only the exact targeted filament record is modified.
     */
    // Modified parameter type to int to safely match the database primary key integer constraint
    public void updateItem(int filamentId, Filament i) {
        String sql = "UPDATE Filaments " +
                     "SET    filamentProductID = ?, " +
                     "       filamentPrice = ?, " +
                     "       filamentCopies = ?, " +
                     "       filamentBrand = ?, " +
                     "       filamentMaterial = ?" +
                     "WHERE  filamentId = ?";

        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Update Operation Successfully.",
                    "DML Execution Failure: ",
                    i.getProductId(),
                    i.getPrice(),
                    i.getCopies(),
                    i.getBrand(),
                    i.getMaterialType(),
                    filamentId); // Passed as integer to isolate the correct auto-increment entry
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Phase 2 & 3: Safe Delete Operation by Product UUID
     * Restricts the removal to a single unique record, preventing accidental data loss.
     */
    // Modified parameter type to int to match the auto-increment data layout criteria
    public void deleteItem(int filamentId) {
        String sql = "DELETE FROM Filaments WHERE filamentId = ?";

        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Delete Operation from backend storage successfully.",
                    "DML Execution Failure: ",
                    filamentId); // Passed as integer to target the specific database row
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