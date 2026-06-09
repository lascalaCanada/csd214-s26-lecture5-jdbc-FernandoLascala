package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Tire extends VehiclePart{
    private int diameter;

    // FIXED: Alternate constructor used by tests to properly pass stock up to VehiclePart parent
    public Tire(String michelin, double price, int stock, int diameter) {
        super(michelin, price, stock); // Passes manufacturer, price, and stock up to VehiclePart
        this.diameter = diameter;
    }


    public Tire(int diameter) {
        this.diameter = diameter;
    }

    public Tire(String manufacturer, double price, int diameter) {
        super(manufacturer, price);
        this.diameter = diameter;
    }

    @Override
    public String toString() {
        return "Tire{" +
                "diameter=" + diameter +
                "} " + super.toString();
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    @Override
    public void initialize(Scanner input) {
        // Pass scanner up to parent
        super.initialize(input);

        System.out.println("Enter Diameter:");
        this.diameter = getInput(input, 0);

    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.println("Enter Diameter:");
        this.diameter = getInput(input, getDiameter());
    }

    /*
     * Phase 3 Refactor: Business logic implementation for physical sales.
     * Instead of a dummy print statement, this decreases the inventory count on every execution.
     */
    @Override
    public void sellItem() {
        if (getCopies() > 0) {
            System.out.println("Processing sale for Tire...");
            setCopies(getCopies() - 1); // Decrements the parent stock field safely
        } else {
            System.out.println("Error: Out of stock for this tire item.");
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

        // 3. Cast to the subclass type to access the tire properties
        Tire other = (Tire) obj;

        // 4. Directly compare integer values representing technical diameter specifications
        return Integer.compare(this.getDiameter(), other.getDiameter()) == 0;
    }

    // HashCode - Combined math generation based on the technical identity attribute
    @Override
    public int hashCode() {
        return java.util.Objects.hash(getDiameter());
    }
}
