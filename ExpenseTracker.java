import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseTracker {

    private static final String FILE_NAME = "expenses.txt";
    private static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        loadExpenses();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Expense Tracker ===");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total by Category");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addExpense(scanner);
                case 2 -> viewExpenses();
                case 3 -> viewTotalByCategory();
                case 4 -> {
                    saveExpenses();
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        expenses.add(new Expense(category, description, amount, date));
        System.out.println("Expense added successfully!");
    }

    private static void viewExpenses() {
        System.out.println("\n=== All Expenses ===");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    private static void viewTotalByCategory() {
        Map<String, Double> totals = new HashMap<>();
        for (Expense expense : expenses) {
            totals.put(expense.getCategory(), 
                       totals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }

        System.out.println("\n=== Total Expenses by Category ===");
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            System.out.printf("%s: $%.2f%n", entry.getKey(), entry.getValue());
        }
    }

    private static void saveExpenses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(expenses);
            System.out.println("Expenses saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            expenses = (List<Expense>) ois.readObject();
            System.out.println("Expenses loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No previous expenses found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }
}

class Expense implements Serializable {
    private final String category;
    private final String description;
    private final double amount;
    private final String date;

    public Expense(String category, String description, double amount, String date) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("Date: %s | Category: %s | Description: %s | Amount: $%.2f",
                date, category, description, amount);
    }
}
