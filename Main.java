import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);
        ExpenseManager expenseManager = new ExpenseManager();
        boolean running = true;

        while (running) {
            displayMenu();
            String choiceText = readInput(scanner, "Enter your choice: ");
            if (choiceText == null) {
                break;
            }
            int choice;

            try {
                choice = Integer.parseInt(choiceText.trim());
            } catch (NumberFormatException exception) {
                System.out.println("Invalid choice. Enter a number from 1 to 9.");
                continue;
            }

            switch (choice) {
                case 1:
                    addExpense(scanner, expenseManager);
                    break;
                case 2:
                    expenseManager.displayAllExpenses();
                    break;
                case 3:
                    searchByTitle(scanner, expenseManager);
                    break;
                case 4:
                    searchByCategory(scanner, expenseManager);
                    break;
                case 5:
                    sortByAmount(scanner, expenseManager);
                    break;
                case 6:
                    sortByDate(scanner, expenseManager);
                    break;
                case 7:
                    expenseManager.calculateTotal();
                    break;
                case 8:
                    deleteExpense(scanner, expenseManager);
                    break;
                case 9:
                    running = false;
                    System.out.println("Thank you for using Daily Expense Analyzer. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Enter a number from 1 to 9.");
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n========== DAILY EXPENSE ANALYZER ==========");
        System.out.println("1. Add Expense");
        System.out.println("2. Display All Expenses");
        System.out.println("3. Search Expense by Title");
        System.out.println("4. Search Expense by Category");
        System.out.println("5. Sort Expenses by Amount");
        System.out.println("6. Sort Expenses by Date");
        System.out.println("7. Calculate Total Expenses");
        System.out.println("8. Delete Expense");
        System.out.println("9. Exit");
        System.out.println("============================================");
    }

    private static void addExpense(Scanner scanner, ExpenseManager expenseManager) {
        String title = readRequiredText(scanner, "Enter Expense Title: ");
        if (title == null) {
            return;
        }

        String category = readRequiredText(scanner, "Enter Category: ");
        if (category == null) {
            return;
        }

        Double amount = readAmount(scanner);
        if (amount == null) {
            return;
        }

        while (true) {
            String date = readInput(scanner, "Enter Date (dd-MM-yyyy): ");
            if (date == null) {
                return;
            }
            try {
                expenseManager.addExpense(title, category, amount, date.trim());
                return;
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid date! Please use dd-MM-yyyy format.");
            }
        }
    }

    private static void searchByTitle(Scanner scanner, ExpenseManager expenseManager) {
        String title = readRequiredText(scanner, "Enter title to search: ");
        if (title != null) {
            expenseManager.searchByTitle(title);
        }
    }

    private static void searchByCategory(Scanner scanner, ExpenseManager expenseManager) {
        String category = readRequiredText(scanner, "Enter category to search: ");
        if (category != null) {
            expenseManager.searchByCategory(category);
        }
    }

    private static void sortByAmount(Scanner scanner, ExpenseManager expenseManager) {
        Integer order = readSortOrder(scanner);
        if (order != null) {
            expenseManager.sortByAmount(order == 1);
        }
    }

    private static void sortByDate(Scanner scanner, ExpenseManager expenseManager) {
        Integer order = readSortOrder(scanner);
        if (order != null) {
            expenseManager.sortByDate(order == 1);
        }
    }

    private static Integer readSortOrder(Scanner scanner) {
        while (true) {
            System.out.println("1. Ascending (low to high / oldest to newest)");
            System.out.println("2. Descending (high to low / newest to oldest)");
            String orderText = readInput(scanner, "Choose sort order: ");
            if (orderText == null) {
                return null;
            }

            try {
                int order = Integer.parseInt(orderText.trim());
                if (order == 1 || order == 2) {
                    return order;
                }
            } catch (NumberFormatException exception) {
                // Show the same input message for non-numeric and out-of-range choices.
            }
            System.out.println("Invalid sort order. Enter 1 or 2.");
        }
    }

    private static void deleteExpense(Scanner scanner, ExpenseManager expenseManager) {
        while (true) {
            String idText = readInput(scanner, "Enter the ID of the expense to delete: ");
            if (idText == null) {
                return;
            }
            try {
                int id = Integer.parseInt(idText.trim());
                expenseManager.deleteExpense(id);
                return;
            } catch (NumberFormatException exception) {
                System.out.println("Invalid ID. Enter a whole number.");
            }
        }
    }

    private static String readRequiredText(Scanner scanner, String prompt) {
        while (true) {
            String input = readInput(scanner, prompt);
            if (input == null) {
                return null;
            }
            String value = input.trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This value cannot be empty.");
        }
    }

    private static Double readAmount(Scanner scanner) {
        while (true) {
            String amountText = readInput(scanner, "Enter Amount: ");
            if (amountText == null) {
                return null;
            }
            try {
                double amount = Double.parseDouble(amountText.trim());
                if (amount > 0 && !Double.isNaN(amount) && !Double.isInfinite(amount)) {
                    return amount;
                }
            } catch (NumberFormatException exception) {
                // Report invalid numeric input below.
            }
            System.out.println("Invalid amount! Please enter a number greater than zero.");
        }
    }

    private static String readInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        System.out.flush();
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }
}
