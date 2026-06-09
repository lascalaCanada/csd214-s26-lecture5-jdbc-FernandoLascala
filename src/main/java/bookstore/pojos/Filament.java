package bookstore.pojos;

import java.util.Scanner;

public class Filament extends PrinterItem implements SaleableItem {
    // Unique field for filament components
    private String materialType;

    // Full constructor passing values up the three levels of inheritance
    public Filament(String title, double price, int copies, String brand, String materialType) {
        super(title, price, copies, brand);
        this.materialType = materialType;
    }

    // FIXED: Alternate constructor used by tests to properly bind the technical attribute
    public Filament(String genericManufacturer, double price, int copies, String materialType) {
        super("Filament Product", price, copies, genericManufacturer);
        this.materialType = materialType;
    }

    // Getter for material type
    public String getMaterialType() {
        return materialType;
    }

    // Setter for material type
    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    // Overriding initialize to process parent setup before local data input
    @Override
    public void initialize(Scanner input) {
        super.initialize(input);
        System.out.print("Enter Material Type (e.g., PLA, ABS): ");
        this.materialType = input.nextLine();
    }

    // Overriding edit to enable modification of all fields down the pipeline
    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.print("Enter New Material Type [" + this.materialType + "]: ");
        String line = input.nextLine();
        if (!line.trim().isEmpty()) {
            this.materialType = line;
        }
    }

    // Polymorphic sales execution tracking inventory updates
    @Override
    public void sellItem() {
        if (getCopies() > 0) {
            System.out.println("Processing sale for Filament brand " + getBrand() + " (" + materialType + ")...");
            setCopies(getCopies() - 1);
        } else {
            System.out.println("Error: Out of stock for this filament item.");
        }
    }

    // Identity method providing explicit representation including super attributes
    @Override
    public String toString() {
        return super.toString() + ", Type: Filament, Material: " + materialType;
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

        // 3. Cast to the subclass type to access the material type property
        Filament other = (Filament) obj;

        // 4. Directly extract and compare material strings using safe logic
        String thisMaterial = this.getMaterialType();
        String otherMaterial = other.getMaterialType();

        if (thisMaterial == null) {
            return otherMaterial == null;
        }

        return thisMaterial.equals(otherMaterial);
    }

    // HashCode - Combined math generation based on the technical identity attribute
    @Override
    public int hashCode() {
        String material = getMaterialType();
        return (material != null) ? material.hashCode() : 0;
    }
}