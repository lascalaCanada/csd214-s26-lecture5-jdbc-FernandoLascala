package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Battery extends VehiclePart{
    private int coldCrankingAmps;

    // FIXED: Alternate constructor used by tests to properly pass stock up to VehiclePart parent
    public Battery(String genericBrand, double price, int stock, int coldCrankingAmps) {
        super(genericBrand, price, stock); // Passes brand, price, and stock up to VehiclePart
        this.coldCrankingAmps = coldCrankingAmps;
    }

    public Battery(int coldCrankingAmps) {
        this.coldCrankingAmps = coldCrankingAmps;
    }

    public Battery(String manufacturer, double price, int coldCrankingAmps) {
        super(manufacturer, price);
        this.coldCrankingAmps = coldCrankingAmps;
    }

    @Override
    public String toString() {
        return "Battery{" +
                "coldCrankingAmps=" + coldCrankingAmps +
                "} " + super.toString();
    }

    public int getColdCrankingAmps() {
        return coldCrankingAmps;
    }

    public void setColdCrankingAmps(int coldCrankingAmps) {
        this.coldCrankingAmps = coldCrankingAmps;
    }

    @Override
    public void initialize(Scanner input) {
        // Pass scanner up to parent
        super.initialize(input);
        System.out.println("Enter Cold Cranking Amps:");
        this.coldCrankingAmps = getInput(input, 0);
    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.println("Enter Cold Cranking Amps:");
        this.coldCrankingAmps = getInput(input, getColdCrankingAmps());
    }

    // Polymorphic sales execution tracking vehicle part inventory updates
    @Override
    public void sellItem() {
        if (getCopies() > 0) {
            System.out.println("Processing sale for Battery...");
            setCopies(getCopies() - 1); // Safely decrements parent field stock
        } else {
            System.out.println("Error: Out of stock for this battery item.");
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

        // 3. Cast to the subclass type to access the battery properties
        Battery other = (Battery) obj;

        // 4. Directly compare integer values representing technical cold cranking amps specifications
        return Integer.compare(this.getColdCrankingAmps(), other.getColdCrankingAmps()) == 0;
    }

    // HashCode - Combined math generation based on the technical identity attribute
    @Override
    public int hashCode() {
        return java.util.Objects.hash(getColdCrankingAmps());
    }


}
