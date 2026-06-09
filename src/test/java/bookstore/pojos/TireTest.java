package bookstore.pojos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TireTest {

    @Test
    void testEquals() {
        // Adapt variables to your exact constructor parameters (Brand, Price, Stock, Width/Size)
        Tire t1 = new Tire("Michelin", 120.00, 8, 225);
        Tire t2 = new Tire("Michelin", 120.00, 8, 225);
        Tire t3 = new Tire("Michelin", 120.00, 8, 195);

        assertEquals(t1, t2, "ERROR: Tires with identical widths must be equal.");
        assertNotEquals(t1, t3, "ERROR: Tires with different widths must not be equal.");
        assertEquals(t1.hashCode(), t2.hashCode(), "ERROR: Identical objects must share the same hashCode.");
    }

    /*
     * Phase 2: Data Integrity (The Constructor Test)
     * Goal: Prove that the "Bucket Brigade" (Constructor Chaining) is working properly.
     */
    @Test
    void testConstructor() {
        // 1. Instantiate using the Loaded Constructor (Passing values for parent and child fields)
        // Parameters: manufacturer (parent), price (parent), diameter (child)
        Tire myTire = new Tire("Michelin", 180.00, 17);

        // 2. Assert: Verify the child stored its local data correctly
        assertEquals(17, myTire.getDiameter(), "ERROR: Child field 'diameter' was not successfully stored.");

        // 3. Assert: Verify the "Bucket Brigade" passed data successfully up to the parent class fields
        assertEquals("Michelin", myTire.getManufacturer(), "ERROR: Parent field 'manufacturer' via Constructor Chaining failed.");
        assertEquals(180.00, myTire.getPrice(), 0.001, "ERROR: Parent field 'price' via Constructor Chaining failed.");
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