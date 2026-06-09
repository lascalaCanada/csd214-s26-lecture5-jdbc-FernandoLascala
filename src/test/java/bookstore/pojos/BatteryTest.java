package bookstore.pojos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryTest {

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
        // Parameters: brand, price, stock, coldCrankingAmps
        Battery b1 = new Battery("ACDelco", 145.00, 10, 650);
        Battery b2 = new Battery("ACDelco", 145.00, 10, 650);

        // 2. Create a different object for counter-proof validation (different CCA)
        Battery b3 = new Battery("ACDelco", 145.00, 10, 800);

        // 3. JUnit 5 Assertions to prove identity resolution for Batteries
        assertEquals(b1, b2, "ERROR: Batteries with identical Cold Cranking Amps must be equal.");
        assertNotEquals(b1, b3, "ERROR: Batteries with different Cold Cranking Amps must not be equal.");
        assertEquals(b1.hashCode(), b2.hashCode(), "ERROR: Identical batteries must share the exact same hashCode.");
    }

    /*
     * Phase 3: Business Logic (The sellItem Test)
     * Goal: Solve the "Stock Issue" from the scenario by ensuring sales decrement inventory.
     */
    @Test
    void testSellItemDecrementsStock() {
        // 1. Create an item with a specific stock of 5
        // Parameters: brand, price, stock, coldCrankingAmps
        Battery item = new Battery("ACDelco", 145.00, 5, 650);

        // 2. Execute the business logic rule
        item.sellItem();

        // 3. Assert: Prove that the system successfully decremented the stock down to 4
        // Note: Change getCopies() to getStock() if that is the name of the getter in your parent class
        assertEquals(4, item.getCopies(), "ERROR: sellItem() did not decrement the stock successfully.");
    }
}