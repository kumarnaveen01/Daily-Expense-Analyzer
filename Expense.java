public class Expense {
    private final int id;
    private final String title;
    private final String category;
    private final double amount;
    private final String date;

    public Expense(int id, String title, String category, double amount, String date) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Title: %s | Category: %s | Amount: %.2f | Date: %s",
                id, title, category, amount, date);
    }
}
