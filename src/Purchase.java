public class Purchase {

    private PurchaseType type;
    private String name;
    private double price;

    public Purchase(PurchaseType type, String name, double price) {
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public PurchaseType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "%s $%.2f".formatted(name, price);
    }
}
