import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Customer {
private String name;
private String email;
private String phone;
private double balance;
private List<Transaction> transactions;

public Customer(String name, String email, String phone, double balance) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.balance = balance;
    this.transactions = new ArrayList<>();
}

public String getName() {
    return name;
}

public String getEmail() {
    return email;
}

public String getPhone() {
    return phone;
}

public double getBalance() {
    return balance;
}

public void setBalance(double balance) {
    this.balance = balance;
}

public List<Transaction> getTransactions() {
    return transactions;
}

public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
    }
}

class Product {
private String name;
private double price;
private int quantity;
private Date expiryDate;

public Product(String name, double price, int quantity, Date expiryDate) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.expiryDate = expiryDate;
}

public String getName() {
    return name;
}

public double getPrice() {
    return price;
}

public int getQuantity() {
    return quantity;
}

public Date getExpiryDate() {
    return expiryDate;
}

public void setQuantity(int quantity) {
    this.quantity = quantity;
    }
}

class Transaction {
private Date date;
private double amount;
private List<Product> products;

public Transaction(Date date, double amount, List<Product> products) {
    this.date = date;
    this.amount = amount;
    this.products = products;
}

public Date getDate() {
    return date;
}

public double getAmount() {
    return amount;
}

public List<Product> getProducts() {
    return products;
}
}

class GroceryStore {
private Map<String, Customer> customers;
private List<Product> products;
private List<Transaction> transactions;
private double profit;
private double loss;

public GroceryStore() {
    customers = new HashMap<>();
    products = new ArrayList<>();
    transactions = new ArrayList<>();
    profit = 0;
    loss = 0;
}

public void addCustomer(Customer customer) {
    customers.put(customer.getEmail(), customer);
}

public Customer getCustomer(String email) {
    return customers.get(email);
}

public void addProduct(Product product) {
    products.add(product);
}

public List<Product> getProducts() {
    return products;
}

public void processTransaction(Transaction transaction) {
    double totalAmount = 0;
    for (Product product : transaction.getProducts()) {
        totalAmount += product.getPrice() * product.getQuantity();
        product.setQuantity(product.getQuantity() - product.getQuantity());
    }

    Customer customer = getCustomer(transaction.getProducts().get(0).getName());
    if (customer != null) {
        customer.setBalance(customer.getBalance() - totalAmount);
        customer.addTransaction(transaction);
    } else {
        System.out.println("Customer not found for the given product.");
    }

    transactions.add(transaction);
    profit += totalAmount;
}
public double getProfit() {
    return profit;
}

public double getLoss() {
    return loss;
}

public void checkExpiredProducts() {
    Date currentDate = new Date();
    for (Product product : products) {
        if (product.getExpiryDate().before(currentDate)) {
               loss += product.getPrice() * product.getQuantity();
            product.setQuantity(0);
            }
        }
    }
}

public class GroceryShoppingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        GroceryStore store = new GroceryStore();

        System.out.print("Enter the number of customers: ");
        int numCustomers = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        for (int i = 0; i < numCustomers; i++) {
            System.out.println("Enter customer " + (i + 1) + " details:");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            System.out.print("Balance: ");
            double balance = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            Customer customer = new Customer(name, email, phone, balance);
            store.addCustomer(customer);
        }

        System.out.print("Enter the number of products: ");
        int numProducts = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        for (int i = 0; i < numProducts; i++) {
            System.out.println("Enter product " + (i + 1) + " details:");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Price: ");
            double price = scanner.nextDouble();
            System.out.print("Quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Expiry date (yyyy-MM-dd): ");
            String expiryDateStr = scanner.nextLine();
            Date expiryDate = null;
            try {
                expiryDate = dateFormat.parse(expiryDateStr);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in the format 'yyyy-MM-dd'.");
                i--;
                continue;
            }

            Product product = new Product(name, price, quantity, expiryDate);
            store.addProduct(product);
        }

        System.out.print("Enter the number of transactions: ");
        int numTransactions = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        for (int i = 0; i < numTransactions; i++) {
            System.out.println("Enter transaction " + (i + 1) + " details:");
            System.out.print("Date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            Date date = null;
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in the format 'yyyy-MM-dd'.");
                i--;
                continue;
            }
            System.out.print("Amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Number of products in the transaction: ");
            int numProductsInTx = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            List<Product> products = new ArrayList<>();
            for (int j = 0; j < numProductsInTx; j++) {
                System.out.print("Enter product " + (j + 1) + " name: ");
                String productName = scanner.nextLine();
                Product product = null;
                for (Product p : store.getProducts()) {
                    if (p.getName().equalsIgnoreCase(productName)) {
                        product = p;
                        break;
                    }
                }
                if (product != null) {
                    products.add(product);
                } else {
                    System.out.println("Product not found in the store.");
                }
            }

            Transaction transaction = new Transaction(date, amount, products);
            store.processTransaction(transaction);
        }

        store.checkExpiredProducts();

        System.out.println("\nProfit: $" + store.getProfit());
        System.out.println("Loss: $" + store.getLoss());
    }
}
