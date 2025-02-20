import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        BudgetManager bm = new BudgetManager(scanner);
        bm.start();
    }
}
