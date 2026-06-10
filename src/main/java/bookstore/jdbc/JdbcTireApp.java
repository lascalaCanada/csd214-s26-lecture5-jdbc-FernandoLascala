package bookstore.jdbc;

import bookstore.pojos.Tire;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTireApp {

    // Database connection infrastructure configurations
    private final Jdbc dbConfig = new Jdbc();

    public static void main(String[] args) {
        JdbcTireApp app = new JdbcTireApp();
        app.createTiresTable();
    }


    /*
     * Phase 1: DDL Schema Design
     * Establishes the 'Tires' table applying custom DBA naming conventions and optimized data types.
     */
    private void createTable() {
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

        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Database Schema: 'Tires' table successfully verified or created.",
                    "DDL Execution Failure: ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Phase 2 & 3: Secure Data Insertion (Outcome 6.4 Compliant)
     */
    public void insertTireItem(Tire i) {
        String sql = "INSERT INTO Tires (tireProductID, tireManufacturer, tirePrice, tireDiameter, "
                + "tireModel, tireSize, tireWidth, tireRimSize, tireSeasonType) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Data Persistence: Tire record successfully saved to the backend repository.",
                    "DML Execution Failure: ",
                    i.getProductId(),
                    i.getManufacturer(),
                    i.getPrice(),
                    i.getDiameter());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Phase 2 & 3: Read Operation & Identity Preservation (Outcome 6.4 Compliant)
     */
    public void listItems() {
        String sql = "SELECT tireProductID, tireManufacturer, tirePrice, tireDiameter FROM Tires";

        // Executing the SQL code in MySQL Database
        // Local function

        try (Connection conn = DriverManager.getConnection(dbConfig.url(), dbConfig.user(), dbConfig.pass());
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String productId = rs.getString("tireProductID");
                String manufacturer = rs.getString("tireManufacturer");
                double price = rs.getDouble("tirePrice");
                int diameter = rs.getInt("tireDiameter");

                Tire tire = new Tire(manufacturer, price, diameter);

                // Rule compliance: Rebinding the persistent state identity string back to the runtime object pointer
                tire.setProductId(productId);

                System.out.println("Hydrated Object -> ID: " + tire.getProductId()
                        + " | Brand: " + tire.getManufacturer()
                        + " | Diameter: " + tire.getDiameter());
            }
        } catch (SQLException e) {
            System.err.println("Read Operation Failure: " + e.getMessage());
        }
    }

    /**
     * Phase 2 & 3: Update Operation
     * Safely updates a persistent tire price matched by its unique UUID index or manufacturer string.
     */
    // Modified parameter type to int to safely align with the primary key column
    public void updatePrice(int tireId, double newPrice) {
        String sql = "UPDATE Tires SET tirePrice = ? WHERE tireId = ?";


        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Update Operation Successfully.",
                    "DML Execution Failure: ",
                    newPrice,
                    tireId); // Passed as an integer parameter to target the matching PK row
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Phase 2 & 3: Delete Operation
     * Safely drops a persistent record using parameterized constraints to isolate execution target.
     */
    // Modified parameter type to int to match the auto-increment column target
    public void deleteItem(int tireId) {
        String sql = "DELETE FROM Tires WHERE tireId = ?";

        // Executing the SQL code in MySQL Database
        // Local function
        try {
            databaseExecs(sql,
                    "Delete Operation from backend storage successfully.",
                    "DML Execution Failure: ",
                    tireId); // Passed as an integer parameter to target the matching PK row
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Phase 1: DDL Schema Design
     * Establishes the 'Tires' table applying custom DBA naming conventions and optimized data types.
     */
    public void createTiresTable() {
        createTable();
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