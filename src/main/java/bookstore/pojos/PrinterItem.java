package bookstore.pojos;

import java.util.Scanner;

public abstract class PrinterItem extends Product {
    // Shared attributes for all 3D printing equipment
    private String brand;
    private String title;
    private double price;
    private int copies;

    // Default constructor generating the standard UUID mapping
    public PrinterItem() {
        super();
    }

    // Constructor chain bucket brigade setup linking local fields
    public PrinterItem(String title, double price, int copies, String brand) {
        super();
        this.title = title;
        this.price = price;
        this.copies = copies;
        this.brand = brand;
    }

    // Accessor and mutator infrastructure mapping variables
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Implementing required interface signature mapping
    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    // Overriding the base setup pipeline sequentially
    @Override
    public void initialize(Scanner input) {
        System.out.print("Enter Product Title: ");
        this.title = input.nextLine();
        System.out.print("Enter Base Price: ");
        while (!input.hasNextDouble()) {
            System.out.print("Invalid format. Enter numeric price: ");
            input.next();
        }
        this.price = input.nextDouble();
        System.out.print("Enter Stock Quantity (Copies): ");
        while (!input.hasNextInt()) {
            System.out.print("Invalid format. Enter whole number: ");
            input.next();
        }
        this.copies = input.nextInt();
        input.nextLine(); // Flush buffer pipeline
        System.out.print("Enter Brand Name: ");
        this.brand = input.nextLine();
    }

    // Modifying baseline data elements over parameters
    @Override
    public void edit(Scanner input) {
        System.out.print("Enter New Title [" + this.title + "]: ");
        String line = input.nextLine();
        if (!line.trim().isEmpty()) {
            this.title = line;
        }

        System.out.print("Enter New Price [" + this.price + "]: ");
        line = input.nextLine();
        if (!line.trim().isEmpty()) {
            try {
                this.price = Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Skipping price update.");
            }
        }

        System.out.print("Enter New Stock Level [" + this.copies + "]: ");
        line = input.nextLine();
        if (!line.trim().isEmpty()) {
            try {
                this.copies = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Skipping stock update.");
            }
        }

        System.out.print("Enter New Brand [" + this.brand + "]: ");
        line = input.nextLine();
        if (!line.trim().isEmpty()) {
            this.brand = line;
        }
    }

    @Override
    public String toString() {
        return super.toString() + "Title: " + title + ", Price: $" + price + ", Stock: " + copies + ", Brand: " + brand;
    }
}