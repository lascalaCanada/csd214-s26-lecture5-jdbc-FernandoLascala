# Bookstore CLI Application

> - [git repository](https://github.com/fcarella/lab1-exercise-fred-carella-csd214-s26)
>  - [based on bookstore git repository: ](https://github.com/fcarella/bookstore-2026-01-30)

- A console-based Java application for managing a bookstore inventory, performing sales, and tracking cash flow. This project demonstrates object-oriented programming concepts including inheritance, polymorphism, and interface implementation in Java 24.

## Features

*   **Inventory Management:**
    *   **Books:** Manage items with Title, Author, Price, and Copies.
    *   **Magazines:** Manage periodicals with Order Quantity and Issue Date.
    *   **Disc Magazines:** Specialized magazines that include a disc.
    *   **Tickets:** Simple saleable items with a description and price.
*   **CRUD Operations:** Add, Edit, and Delete items from the inventory.
*   **Sales System:** Sell items to decrement inventory count and increase the Cash Till total.
*   **Data Generation:** Uses `JavaFaker` to populate the inventory with realistic dummy data.
*   **Menu System:** Interactive console menu for navigation.

## Class Hierarchy

![Class Diagram](documentation/bookstore-2026-01-30-142617.png)

The hierarchy implements the following structure:
*   **SaleableItem (Interface):** Defines `sellItem()` and `getPrice()`.
*   **Editable (Abstract):** Handles console input/output and parsing.
*   **Publication:** Base class for Books and Magazines (Title, Price, Copies).

## Prerequisites

*   **Java JDK:** Version 24
*   **Maven:** 3.6+

## Dependencies

*   [JavaFaker](https://github.com/DiUS/java-faker) (1.0.2): For generating random test data.
*   [JUnit 5](https://junit.org/junit5/) (5.10.0): For unit testing.

## How to Run

1.  **Compile the project:**
    ```bash
    mvn clean compile
    ```

2.  **Run the application:**
    ```bash
    mvn exec:java -Dexec.mainClass="csd214.bookstore.Main"
    ```

## Usage

Upon starting, the application will populate the list with random data. You will see the following menu:

```text
***********************
 1. Add Items
 2. Edit Items
 3. Delete Items
 4. Sell item(s)
 5. List items
99. Quit
***********************
```

*   **Add Items:** Choose a specific type (Book, Magazine, etc.) and follow the prompts.
*   **Edit Items:** Select an index from the list to modify fields.
*   **Sell Items:** Select an index to sell. This decreases the 'Copies' count (for Publications) and adds the price to the internal Cash Till.

## Running Tests

Unit tests are implemented using JUnit 5 to verify the logic of POJOs and input mocking.

Run the tests using Maven:

```bash
mvn test
```

## Project Structure

```
src/
├── main/
│   └── java/
│       └── csd214/
│           └── bookstore/
│               ├── Main.java           # Entry point
│               ├── App.java            # Controller / Menu Logic
│               └── pojos/              # Data Models
│                   ├── Editable.java
│                   ├── SaleableItem.java
│                   ├── Product.java
│                   ├── Publication.java
│                   ├── Book.java
│                   ├── Magazine.java
│                   ├── DiscMag.java
│                   ├── Ticket.java
│                   └── CashTill.java
└── test/
    └── java/
        └── csd214/
            └── bookstore/
                └── pojos/              # Unit Tests
```

*________________________________________________________________________________________*

# LAB 1 - All statements

## PHASE 1

### STEP 1 (Niche Selection & Analysis)
### ***Analysis***

* **Problem Statement**

    The 3D Printer Store specializes in selling equipment, materials, and digital assets for hobbyists and professionals working with additive manufacturing. The store offers 3D printers, filaments, nozzles, accessories, and premium STL files for custom printing. Customers can also purchase pre‑printed customized pieces made on demand. The goal of the store is to provide a complete ecosystem for users who want reliable hardware, high‑quality materials, and exclusive digital models. 

### ***Noun / Verb Table***

| **Nouns (Potential Classes / Attributes)** | **Verbs (Potential Methods)** |
| --- | --- |
| 3D Printer | Print() |
| Filament | LoadMaterial() |
| Nozzle | Replace() |
| Accessory | Install() |
| STL File | Download() |
| Custom Printed Piece | Customize() |

### ***Synonym Identification***
* Filament = Material
* Nozzle = Extruder Tip

## STEP 2
### AI‑Assisted Architectural Blueprinting

#### ***Abstract Class (extends Product)***
* Class Name: PrinterItem
* Parent Class: Product
* Unique Field (common to all items in this niche):
* * String brand

#### ***Concrete Class 1***
* Class Name: Filament
* Parent Class: PrinterItem
* Unique Field:
    * String materialType  
      (e.g., PLA, ABS, PETG)

#### ***Concrete Class 2***
* Class Name: Nozzle
* Parent Class: PrinterItem
* Unique Field:
    * double diameter  
      (e.g., 0.4mm, 0.6mm, 0.8mm)

## STEP 3
### Implementation Standards (Java Bean Requirement)

#### ***Java Bean Standards for All New Classes***
1. **Encapsulation**

   All attributes must be declared as private, ensuring full encapsulation.
   Every field must have corresponding public getters and setters to allow controlled access and modification.

2. **No-Argument Constructor**

   Each class must include a no‑argument constructor.
   This is required for frameworks such as Hibernate/JPA (used in Lab 4) and ensures compatibility with reflection‑based instantiation.

3. **Loaded Constructor with Constructor Chaining**

   Each class must also include a loaded constructor that accepts all fields.
   This constructor must use super() to pass inherited attributes up the hierarchy, following the “Bucket Brigade” constructor‑chaining model from Lecture 3.

4. **Object Identity & Representation**

   Use IntelliJ’s Generate feature (Alt+Insert) to create:

    * toString()  
      Must include a call to super.toString() to ensure inherited fields are represented.
    * equals() and hashCode()  
      Required for comparing niche items, collections, and future persistence logic.

## STEP 4
### Implementation Requirements

### ***Abstract Parent Class Requirements***
Your abstract class (in your case: PrinterItem) must:

* **Extend Product**  
  This ensures it inherits base fields such as Title, Price, and Copies.

    * Contain the shared field

      Example:
        * String brand

* **Override initialize(Scanner input)**  
  Must call:

  **super.initialize(input)**  
  so that Product fields are not lost.

* **Override edit(Scanner input)**  
  Must also call:

  **super.edit(input)**  
  to allow editing inherited fields before editing niche‑specific ones.

### Concrete Child Class Requirements
Each concrete class (e.g., Filament, Nozzle) must:

* **Extend your abstract parent (PrinterItem)**
* **Contain its unique field**  
  Examples:

    * Filament > String materialType
    * Nozzle > double diameter

* **Override initialize(Scanner input)**

  Must call:
  **super.initialize(input)**  
  before reading its own fields.

* **Override edit(Scanner input)**  
  Must call:
  **super.edit(input)**  
  before editing its own fields.

* **Override sellItem()**
    * For items with quantity (like Filament): decrement copies.
    * For items without quantity (like STL files, if you add them later): simply return price.
    * Always interact with CashTill.

## PHASE 3: Implementation (Java Beans)

### **A. Abstract Niche Parent (PrinterItem.java)**
Your abstract niche parent class must follow these rules:
* **Must be abstract**

  This prevents direct instantiation and enforces specialization.

* **Must extend Product**

  Ensures inheritance of base fields such as title, price, and copies.

* **Must include the shared field**

  Example for your niche:
    * brand: String

* **Encapsulation**

  All fields must be private, with public getters and setters.

* **No‑Arg Constructor**

  Required for frameworks and reflection (Lab 4 compatibility).

* **Loaded Constructor**

  Must accept all fields of PrinterItem and call:
  **super(...)**
  to pass Product fields up the constructor chain (“Bucket Brigade”).

* **Override initialize(Scanner input)**

  Must call:
  **super.initialize(input)**
  before reading niche‑specific fields.

* **Override edit(Scanner input)**

  Must call:
  **super.edit(input)**
  before editing niche‑specific fields.

### ***B. Concrete Niche Items (Filament.java, Nozzle.java)***
Each concrete class must follow these rules:

* **Must extend PrinterItem**

Ensures inheritance of shared niche fields and Product fields.

* **Must include its unique field**

  Examples:
    * Filament → materialType: String
    * Nozzle → diameter: double

* **Encapsulation**

  All fields private, with public getters and setters.

* **No‑Arg Constructor**

  Required for Java Bean compliance.

* **Loaded Constructor with Constructor Chaining**

  Must accept all fields (including inherited ones) and call:
  **super(...)**
  to pass data up the hierarchy.

* **Override initialize(Scanner input)**

  Must call:
  **super.initialize(input)**
  before reading its own fields.

* **Override edit(Scanner input)**

  Must call:
  **super.edit(input)**
  before editing its own fields.

* **Override sellItem()

  Must implement niche‑specific behavior.
  Example:
  “Processing sale for Filament…”
  or
  “Heating nozzle and processing sale…”

* **Identity Methods (Lab 3 Prep)**

  Must generate:
    * toString() (including super.toString())
    * equals()
    * hashCode()

*________________________________________________________________________________________*
