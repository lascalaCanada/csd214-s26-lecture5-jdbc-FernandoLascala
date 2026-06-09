package bookstore.pojos;

import java.util.Scanner;

public class Nozzle extends PrinterItem implements SaleableItem {
    // Unique numeric field for printer nozzle hardware
    private double diameter;

    /* Required blank constructor for data mapping requirements
    public Nozzle(String genericManufacturer, double v, int i, double v1) {
        super();
    }
    */

    // FIXED: Alternate constructor used by tests to properly bind the technical attribute
    public Nozzle(String genericManufacturer, double price, int stock, double diameter) {
        // Pass attributes up to PrinterItem / Product hierarchy
        super("Nozzle Item", price, stock, genericManufacturer);
        this.diameter = diameter;
    }

    // Loaded bucket brigade constructor linking values across the codebase
    public Nozzle(String title, double price, int copies, String brand, double diameter) {
        super(title, price, copies, brand);
        this.diameter = diameter;
    }

    // Getter for component specifications
    public double getDiameter() {
        return diameter;
    }

    // Setter for component specifications
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    // Processing interactive prompt setup via the inheritance architecture
    @Override
    public void initialize(Scanner input) {
        super.initialize(input);
        System.out.print("Enter Nozzle Diameter (e.g., 0.4, 0.6): ");
        while (!input.hasNextDouble()) {
            System.out.print("Invalid input. Enter a decimal number for diameter: ");
            input.next();
        }
        this.diameter = input.nextDouble();
        input.nextLine(); // Clear scanner buffer space
    }

    // Editing interactive fields tracking inherited and local scope data
    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.print("Enter New Diameter [" + this.diameter + "]: ");
        String line = input.nextLine();
        if (!line.trim().isEmpty()) {
            try {
                this.diameter = Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric format. Keeping original value.");
            }
        }
    }

    // Polymorphic sales execution adhering to interface parameters
    @Override
    public void sellItem() {
        if (getCopies() > 0) {
            System.out.println("Heating nozzle and processing sale for size " + diameter + "mm...");
            setCopies(getCopies() - 1);
        } else {
            System.out.println("Error: Out of stock for this nozzle size.");
        }
    }

    /*
     * Equals - Validates item identity based on its technical specifications to solve the Clone Problem
     */
    @Override
    public boolean equals(Object obj) {
        // 1. Check if both point to the exact same memory address
        if (this == obj) return true;

        // 2. Safeguard against null objects or class type mismatch
        if (obj == null || getClass() != obj.getClass()) return false;

        // 3. Cast to the subclass type to access the nozzle properties
        Nozzle other = (Nozzle) obj;

        // 4. Safely compare double primitive types using Double.compare to avoid precision issues
        return Double.compare(this.getDiameter(), other.getDiameter()) == 0;
    }

    // HashCode - Combined math generation based on the technical identity attribute
    @Override
    public int hashCode() {
        return Double.hashCode(getDiameter());
    }
}