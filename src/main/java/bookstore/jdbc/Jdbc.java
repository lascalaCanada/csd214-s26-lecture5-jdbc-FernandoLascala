package bookstore.jdbc;

/**
 * Phase 1: Infrastructure Configuration Record
 * Centralizes database connection parameters to eliminate redundancy across the persistence layer.
 */
public record Jdbc(
        String url,
        String user,
        String pass
) {
    // Constructor holding the fixed production database credentials
    public Jdbc() {
        this(
                "jdbc:mysql://localhost:3333/bookstore",
                "root",
                "itstudies12345"
        );
    }
}