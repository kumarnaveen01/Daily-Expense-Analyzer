import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseManager {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final int TABLE_WIDTH = 76;
    private final ArrayList<Expense> expenses = new ArrayList<>();
    private int nextExpenseId = 1;

    public void addExpense(String title, String category, double amount, String date) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense title cannot be empty.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense category cannot be empty.");
        }
        if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        parseDate(date);

        expenses.add(new Expense(nextExpenseId, title.trim(), category.trim(), amount, date.trim()));
        nextExpenseId++;
        System.out.println("Expense added successfully!");
    }

    public void displayAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses have been recorded yet.");
            return;
        }

        System.out.println();
        printExpenseHeading();
        printTableHeader();
        for (int i = 0; i < expenses.size(); i++) {
            printExpense(expenses.get(i));
        }
        printHorizontalLine('=');
    }

    public void searchByTitle(String title) {
        search(title, true);
    }

    public void searchByCategory(String category) {
        search(category, false);
    }

    private void search(String searchText, boolean searchTitle) {
        if (searchText == null || searchText.trim().isEmpty()) {
            System.out.println("Search text cannot be empty.");
            return;
        }

        boolean found = false;
        String fieldName = searchTitle ? "title" : "category";
        String searchValue = searchText.trim();

        System.out.println("\nSearch results for " + fieldName + " \"" + searchValue + "\":");
        printTableHeader();
        // Check every record in the ArrayList; matching is case-insensitive and exact.
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            String value = searchTitle ? expense.getTitle() : expense.getCategory();
            if (value.equalsIgnoreCase(searchValue)) {
                printExpense(expense);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching expenses found.");
        }
        printHorizontalLine('=');
    }

    public void sortByAmount(boolean ascending) {
        // Bubble Sort swaps neighboring expenses until they are in the requested order.
        for (int i = 0; i < expenses.size() - 1; i++) {
            for (int j = 0; j < expenses.size() - 1 - i; j++) {
                double firstAmount = expenses.get(j).getAmount();
                double secondAmount = expenses.get(j + 1).getAmount();
                boolean shouldSwap = ascending
                        ? firstAmount > secondAmount
                        : firstAmount < secondAmount;
                if (shouldSwap) {
                    swapExpenses(j, j + 1);
                }
            }
        }

        System.out.println("Expenses sorted by amount (" + (ascending ? "low to high" : "high to low") + ").");
        displayAllExpenses();
    }

    public void sortByDate(boolean ascending) {
        // Parsed dates are compared so calendar order is correct across months and years.
        for (int i = 0; i < expenses.size() - 1; i++) {
            for (int j = 0; j < expenses.size() - 1 - i; j++) {
                Date firstDate = parseDate(expenses.get(j).getDate());
                Date secondDate = parseDate(expenses.get(j + 1).getDate());
                boolean shouldSwap = ascending
                        ? firstDate.after(secondDate)
                        : firstDate.before(secondDate);
                if (shouldSwap) {
                    swapExpenses(j, j + 1);
                }
            }
        }

        System.out.println("Expenses sorted by date (" + (ascending ? "oldest to newest" : "newest to oldest") + ").");
        displayAllExpenses();
    }

    public void calculateTotal() {
        double total = 0;
        for (int i = 0; i < expenses.size(); i++) {
            total += expenses.get(i).getAmount();
        }
        System.out.printf("Total Expense: %.2f%n", total);
    }

    public void deleteExpense(int id) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == id) {
                expenses.remove(i);
                System.out.println("Expense deleted successfully.");
                return;
            }
        }
        System.out.println("No expense found with ID " + id + ".");
    }

    private void swapExpenses(int firstIndex, int secondIndex) {
        Expense temporary = expenses.get(firstIndex);
        expenses.set(firstIndex, expenses.get(secondIndex));
        expenses.set(secondIndex, temporary);
    }

    private Date parseDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be empty. Use dd-MM-yyyy.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setLenient(false);
        String dateValue = date.trim();

        try {
            Date parsedDate = dateFormat.parse(dateValue);
            if (!dateFormat.format(parsedDate).equals(dateValue)) {
                throw new IllegalArgumentException("Invalid date. Use dd-MM-yyyy.");
            }
            return parsedDate;
        } catch (ParseException exception) {
            throw new IllegalArgumentException("Invalid date. Use dd-MM-yyyy.", exception);
        }
    }

    private void printTableHeader() {
        System.out.printf("%-6s %-22s %-18s %-14s %-12s%n",
                "ID", "TITLE", "CATEGORY", "AMOUNT", "DATE");
        printHorizontalLine('-');
    }

    private void printExpense(Expense expense) {
        System.out.printf("%-6d %-22.22s %-18.18s %-14.2f %-12s%n",
                expense.getId(), expense.getTitle(), expense.getCategory(),
                expense.getAmount(), expense.getDate());
    }

    private void printExpenseHeading() {
        int sideWidth = (TABLE_WIDTH - " EXPENSE RECORDS ".length()) / 2;
        printRepeatedCharacter('=', sideWidth);
        System.out.print(" EXPENSE RECORDS ");
        printRepeatedCharacter('=', TABLE_WIDTH - sideWidth - " EXPENSE RECORDS ".length());
        System.out.println();
    }

    private void printHorizontalLine(char character) {
        printRepeatedCharacter(character, TABLE_WIDTH);
        System.out.println();
    }

    private void printRepeatedCharacter(char character, int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(character);
        }
    }
}
