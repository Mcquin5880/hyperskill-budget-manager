import java.util.*;

public class BudgetManager {

    private Scanner scanner;
    private Set<Purchase> purchases;
    private Map<PurchaseType, List<Purchase>> categorizedPurchases;
    private double income;
    private double expenses;

    public BudgetManager(Scanner scanner) {

        this.scanner = scanner;
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
}
