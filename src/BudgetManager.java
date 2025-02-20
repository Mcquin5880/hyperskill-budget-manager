import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BudgetManager {

    private Scanner scanner;
    private Set<Purchase> purchases;
    private Map<PurchaseType, List<Purchase>> categorizedPurchases;
    private double income;
    private double expenses;

    public BudgetManager() {

        this.scanner = new Scanner(System.in);
        this.purchases = new LinkedHashSet<>();

        this.categorizedPurchases = new EnumMap<>(PurchaseType.class);
        for (PurchaseType type : PurchaseType.values()) {
            categorizedPurchases.put(type, new ArrayList<>());
        }

        this.income = 0;
        this.expenses = 0;
    }

    public void start() {

        while (true) {
            printMainMenu();
            int action = Integer.parseInt(scanner.nextLine());

            if (action == 0) {
                System.out.println("\nBye!");
                break;
            }

            switch (action) {
                case 1 -> {
                    System.out.println("\nEnter income:");
                    income += Double.parseDouble(scanner.nextLine());
                    System.out.println("Income was added!");
                }
                case 2 -> handlePurchase();
                case 3 -> handleShowPurchases();
                case 4 -> System.out.printf("%nBalance: $%.2f%n", income);
                case 5 -> savePurchases();
                case 6 -> loadPurchases();
                default -> System.out.println("Invalid command");
            }
        }
    }

    private void handlePurchase() {

        while (true) {
            printPurchaseMenu();
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 5) break;

            PurchaseType type = getPurchaseType(input);
            System.out.println("\nEnter purchase name:");
            String name = scanner.nextLine();
            System.out.println("Enter its price:");
            double price = Double.parseDouble(scanner.nextLine());

            Purchase purchase = new Purchase(type, name, price);
            purchases.add(purchase);
            categorizedPurchases.get(type).add(purchase);
            income -= price;
            expenses += price;
            System.out.println("Purchase was added!");
        }
    }

    private void handleShowPurchases() {

        if (purchases.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        while (true) {
            printShowPurchasesMenu();
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 6) break;

            if (input == 5) {
                printAllPurchases();
            } else {
                PurchaseType type = getPurchaseType(input);
                printPurchasesByCategory(type);
            }
        }
    }

    private void printAllPurchases() {
        System.out.println("\nAll:");
        purchases.forEach(System.out::println);
        System.out.printf("Total sum: $%.2f%n", expenses);
    }

    private void printPurchasesByCategory(PurchaseType type) {

        List<Purchase> categoryPurchases = categorizedPurchases.get(type);
        System.out.println("\n" + type + ":");

        if (categoryPurchases.isEmpty()) {
            System.out.println("The purchase list is empty!");
            return;
        }

        double categorySum = categoryPurchases.stream()
                .mapToDouble(Purchase::getPrice)
                .sum();

        categoryPurchases.forEach(System.out::println);
        System.out.printf("Total sum: $%.2f%n", categorySum);
    }

    private PurchaseType getPurchaseType(int category) {
        return switch (category) {
            case 1 -> PurchaseType.FOOD;
            case 2 -> PurchaseType.CLOTHES;
            case 3 -> PurchaseType.ENTERTAINMENT;
            default -> PurchaseType.OTHER;
        };
    }

    private void printMainMenu() {
        System.out.println("\nChoose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("0) Exit");
    }

    private void printPurchaseMenu() {
        System.out.println("\nChoose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) Back");
    }

    private void printShowPurchasesMenu() {
        System.out.println("\nChoose the type of purchases");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) All");
        System.out.println("6) Back");
    }

    private void savePurchases() {

        try (PrintWriter writer = new PrintWriter("purchases.txt")) {
            writer.println(income);
            for (Purchase p : purchases) {
                writer.println(p.getType().name() + ";" + p.getName() + ";" + p.getPrice());
            }
            System.out.println("\nPurchases were saved!");
        } catch (IOException e) {
            System.out.println("An error occurred while saving.");
        }
    }

    private void loadPurchases() {

        File file = new File("purchases.txt");

        if (!file.exists()) {
            System.out.println("File not found!");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {

            if (fileScanner.hasNextLine()) {
                income = Double.parseDouble(fileScanner.nextLine());
            }

            purchases.clear();
            for (List<Purchase> list : categorizedPurchases.values()) {
                list.clear();
            }
            expenses = 0;

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length != 3) continue;

                PurchaseType type = PurchaseType.valueOf(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);

                Purchase p = new Purchase(type, name, price);
                purchases.add(p);
                categorizedPurchases.get(type).add(p);
                expenses += price;
            }
            System.out.println("\nPurchases were loaded!");
        } catch (IOException e) {
            System.out.println("An error occurred while loading.");
        }
    }
}
