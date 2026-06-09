package bookstore; // Root pack to easily interact with App.java context

import bookstore.pojos.App;
import bookstore.pojos.Tire;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    /*
     * Phase 4 Integration Test: Full Application Flow
     * Uses Dependency Injection to stream keyboard keys to System.in, preventing automated test freezes.
     */
    @Test
    void testAppFlow_AddCustomNiche() {
        // 1. Scripting: Define a predefined string stream mimicking real physical user keypresses
        StringBuilder script = new StringBuilder();

        // Menu Step 1: Tell main menu we want to "Add Items"
        script.append("1\n");

        // Menu Step 2: Select item "5" from the sub-menu to instantiate a new "Tire"
        script.append("5\n");

        // Menu Step 3: Stream consecutive inputs required by the sequential Scanner initialization pipeline
        script.append("Michelin\n");          // Captures Manufacturer
        script.append("189.90\n");            // Captures Price
        script.append("17\n");                 // Captures Child Niche Field (Diameter)

        // Menu Step 4: Exit the active sub-menu to return to the root interface
        script.append("99\n");

        // Menu Step 5: Input termination choice "99" to kill the core application loop safely
        script.append("99\n");

        // 2. Dependency Injection: Substitute standard system hardware input stream with our scripted stream
        System.setIn(new ByteArrayInputStream(script.toString().getBytes()));

        // 3. Execution: Create a clean environment instance and trigger the core entry framework
        App app = new App();
        app.run(); // The application loop runs entirely on memory inputs without locking or crashing

        // 4. Verification: Query the backend storage repository using object identity mapping rules
        // We initialize a temporary validation clone containing the exact data injected into the application
        Tire lookupSample = new Tire("Michelin", 189.90, 1, 17);

        // Assert: Confirm that the system successfully added and saved the item into the internal data store
        assertNotNull(app.findItem(lookupSample), "ERROR: Integration flow failed. Custom niche item was not saved or found.");
    }
}