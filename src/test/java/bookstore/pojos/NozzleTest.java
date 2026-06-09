package bookstore.pojos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NozzleTest {

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
        // 1. Create two identical objects (Clones with the same technical specifications)
        // Ensure parameters match your exact Nozzle constructor signature
        Nozzle n1 = new Nozzle("Generic Manufacturer", 25.00, 5, 0.4);
        Nozzle n2 = new Nozzle("Generic Manufacturer", 25.00, 5, 0.4);

        // 2. Create a different object for counter-proof validation (different diameter)
        Nozzle n3 = new Nozzle("Generic Manufacturer", 25.00, 5, 0.6);

        // 3. JUnit 5 Assertions to prove identity resolution for Nozzles
        assertEquals(n1, n2, "ERROR: Nozzles with identical diameters must be equal.");
        assertNotEquals(n1, n3, "ERROR: Nozzles with different diameters must not be equal.");
        assertEquals(n1.hashCode(), n2.hashCode(), "ERROR: Identical nozzles must share the exact same hashCode.");
    }
}