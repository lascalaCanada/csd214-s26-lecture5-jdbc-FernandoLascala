package bookstore.pojos;

import bookstore.pojos.*;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {
    private List<SaleableItem> items = new ArrayList<>();
    private CashTill cashTill = new CashTill();
    private Scanner input = new Scanner(System.in);

    public void run() {
        populate();
        int choice = 0;
        while (choice != 99) {
            System.out.println("\n***********************");
            System.out.println(" 1. Add Items");
            System.out.println(" 2. Edit Items");
            System.out.println(" 3. Delete Items");
            System.out.println(" 4. Sell item(s)");
            System.out.println(" 5. List items");
            System.out.println("99. Quit");
            System.out.println("***********************");
            System.out.print("Enter choice: \n");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                choice = 0;
            }

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    editItem();
                    break;
                case 3:
                    deleteItem();
                    break;
                case 4:
                    sellItem();
                    break;
                case 5:
                    listAny();
                    break;
                case 99:
                    // Exit
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void addItem() {
        int choice = 0;
        while (choice != 99) {
            System.out.println("\nAdd an item\n");
            System.out.println("1. Add Book");
            System.out.println("2. Add Magazine");
            System.out.println("3. Add DiscMag");
            System.out.println("4. Add Ticket");
            System.out.println("5. Add Tire");
            // 3D Printer
            System.out.println("6. Add Filament (3D Print)");
            System.out.println("7. Add Nozzle (3D Print)");
            // 3D Printer
            System.out.println("99. Exit");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            if (choice == 99) return;

            SaleableItem item = null;
            switch(choice) {
                case 1: item = new Book(); break;
                case 2: item = new Magazine(); break;
                case 3: item = new DiscMag(); break;
                case 4: item = new Ticket(); break;
                case 5: item = new Tire("Michelin", 120.00, 8, 225); break;
                // 3D Printer
                case 6: item = new Filament("Generic Manufacturer", 89.90, 10, "PLA"); break;
                case 7: item = new Nozzle("Generic Manufacturer", 25.00, 5, 0.4); break;
                // 3D Printer
                default: System.out.println("Invalid selection."); continue;
            }

            if(item instanceof Editable) {
                // PASS THE SHARED SCANNER
                ((Editable)item).initialize(this.input);
            }

            addItem(item);
        }
    }

    public void addItem(SaleableItem item) {
        items.add(item);
    }

    public void listAny() {
        int choice = 0;
        while (choice != 99) {
            System.out.println("\nAll Items");
            System.out.println("-----------");
            System.out.println("List");
            System.out.println("1. All");
            System.out.println("2. Books");
            System.out.println("3. Magazines");
            System.out.println("4. DiscMags");
            System.out.println("5. Tickets");
            System.out.println("6. Tire");
            // 3D Printer
            System.out.println("7. Filament");
            System.out.println("8. Nozzle");
            // 3D Printer
            System.out.println("99. Exit");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            if (choice == 99) return;

            Class<?> filter = null;
            switch(choice) {
                case 1: filter = null; break;
                case 2: filter = Book.class; break;
                case 3: filter = Magazine.class; break;
                case 4: filter = DiscMag.class; break;
                case 5: filter = Ticket.class; break;
                case 6: filter = Tire.class; break;
                case 7: filter = Filament.class; break;
                case 8: filter = Nozzle.class; break;
                default: System.out.println("Invalid selection."); continue;
            }

            for (SaleableItem i : items) {
                boolean show = false;
                if (filter == null) {
                    show = true;
                } else {
                    if (filter == Magazine.class && i instanceof DiscMag) {
                        show = false;
                    } else if (filter.isInstance(i)) {
                        show = true;
                    }
                }

                if (show) {
                    listI(i);
                }
            }
        }
    }

    public void listI(Object o) {
        System.out.println(o.toString());
    }

    public void editItem() {
        System.out.println("Select item index to edit (0 to " + (items.size() - 1) + "):");
        for(int i=0; i<items.size(); i++) {
            System.out.println(i + ". " + items.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < items.size()) {
                SaleableItem item = items.get(idx);
                if (item instanceof Editable) {
                    editItem((Editable) item);
                } else {
                    System.out.println("Item is not editable.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    public void editItem(Editable item) {
        // PASS THE SHARED SCANNER
        item.edit(this.input);
    }

    public void deleteItem() {
        System.out.println("Select item index to delete:");
        for(int i=0; i<items.size(); i++) {
            System.out.println(i + ". " + items.get(i));
        }
        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < items.size()) {
                items.remove(idx);
                System.out.println("Item deleted.");
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    public void sellItem() {
        System.out.println("Select item index to sell:");
        for(int i=0; i<items.size(); i++) {
            System.out.println(i + ". " + items.get(i));
        }
        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < items.size()) {
                SaleableItem item = items.get(idx);
                cashTill.sellItem(item);
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    public boolean findItemExists(SaleableItem item) {
        return items.contains(item);
    }

    public SaleableItem findItem(SaleableItem item) {
        int index = items.indexOf(item);
        if (index != -1) return items.get(index);
        return null;
    }

    public SaleableItem getItem(SaleableItem item) {
        return findItem(item);
    }

    public void populate() {
        System.out.println("Populating data with JavaFaker...");
        Faker faker = new Faker();

        for (int i = 0; i < 2; i++) {
            // Book
            Book b = new Book(
                    faker.book().author(),
                    faker.book().title(),
                    faker.number().randomDouble(2, 10, 50), // Price
                    faker.number().numberBetween(1, 20)     // Copies
            );
            addItem(b);

            // Magazine
            Magazine m = new Magazine(
                    faker.number().numberBetween(100, 500), // Order Qty
                    faker.date().past(30, TimeUnit.DAYS),   // Date
                    faker.book().title() + " Monthly",      // Title
                    faker.number().randomDouble(2, 5, 15),  // Price
                    faker.number().numberBetween(5, 50)     // Copies
            );
            addItem(m);

            // DiscMag
            DiscMag dm = new DiscMag(
                    faker.bool().bool(),                    // Has Disc
                    faker.number().numberBetween(50, 200),  // Order Qty
                    faker.date().past(60, TimeUnit.DAYS),   // Date
                    "Tech Disc: " + faker.app().name(),     // Title
                    faker.number().randomDouble(2, 10, 25), // Price
                    faker.number().numberBetween(5, 30)     // Copies
            );
            addItem(dm);

            // Ticket
            Ticket t = new Ticket();
            t.setDescription("Concert: " + faker.rockBand().name());
            t.setPrice(faker.number().randomDouble(2, 50, 150));
            addItem(t);

            // Tire
            Tire tire = new Tire(
                    faker.commerce().productName() + " Tires", // manufacturer (e.g., "Michelin Tires")
                    faker.number().randomDouble(2, 80, 350),   // price (e.g., $80.00 to $350.00)
                    faker.number().numberBetween(15, 22)     // diameter (e.g., 15 to 22 inches)
            );
            addItem(tire);

            // 3D Printer
            // Filament Seeding Setup
            Filament filament = new Filament(
                    faker.commerce().color() + " Filament",
                    faker.number().randomDouble(2, 20, 60),
                    faker.number().numberBetween(5, 25),
                    faker.company().name(),
                    faker.options().option("PLA", "ABS", "PETG", "TPU")
            );
            addItem(filament);

            // Nozzle Seeding Setup
            Nozzle nozzle = new Nozzle(
                    "High Flow Brass Nozzle",
                    faker.number().randomDouble(2, 8, 35),
                    faker.number().numberBetween(10, 40),
                    faker.company().name(),
                    faker.options().option(0.4, 0.6, 0.8)
            );
            addItem(nozzle);
            // 3D Printer

        }
    }
}