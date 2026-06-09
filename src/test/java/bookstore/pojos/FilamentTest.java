package bookstore.pojos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilamentTest {

    @BeforeEach
    void setUp() {
        // No setup required for basic POJO identity testing
    }

    @AfterEach
    void tearDown() {
        // No teardown required after basic POJO identity testing
    }

    @Test
    void testEquals() {
        // FIX: Using the 5-parameter constructor to correctly assign the materialType field
        // Parameters: title, price, copies, brand, materialType
        Filament f1 = new Filament("Premium Filament", 89.90, 10, "Generic Manufacturer", "PLA");
        Filament f2 = new Filament("Premium Filament", 89.90, 10, "Generic Manufacturer", "PLA");

        // Counter-proof object with a completely different material type
        Filament f3 = new Filament("Premium Filament", 89.90, 10, "Generic Manufacturer", "ABS");

        // JUnit 5 Assertions to prove identity resolution and fix the "Clone Problem"
        assertEquals(f1, f2, "ERROR: Objects with identical technical attributes must be equal.");
        assertNotEquals(f1, f3, "ERROR: Objects with different technical attributes must not be equal.");
        assertEquals(f1.hashCode(), f2.hashCode(), "ERROR: Identical objects must share the exact same hashCode.");
    }
}