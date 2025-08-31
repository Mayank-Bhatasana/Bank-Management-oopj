import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

// Main class to run the application
class Main {

    static int userCount = 0;
    static final int MAX_USERS = 100;
    static final Scanner sc = new Scanner(System.in);
    static final User[] users = new User[MAX_USERS];

    // Regex patterns for validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    // Requires at least 8 chars, 1 uppercase, 1 lowercase, 1 digit
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";


    public static void main(String[] args) {
        while (true) {
            clearScreen();
            System.out.println("🏦 Welcome to the Bank Management System! 🏦");
            System.out.println("1. Register User");
            System.out.println("2. Login User");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = readIntSafe();

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> {
                    int loggedInUserIndex = loginUser();
                    if (loggedInUserIndex != -1) {
                        userMenu(loggedInUserIndex);
                    } else {
                        System.out.println("❌ Wrong username or password. Please try again.");
                        pause();
                    }
                }
                case 3 -> {
                    clearScreen();
                    System.out.println("Thank you for using the Bank Management System. Goodbye! 👋");
                    return;
                }
                default -> {
                    System.out.println("Invalid choice. Please select a valid option.");
                    pause();
                }
            }
        }
    }

    /* ---------------- Registration & Login ---------------- */

    public static void registerUser() {
        clearScreen();
        System.out.println("--- 📝 User Registration ---");

        if (userCount >= MAX_USERS) {
            System.out.println("User capacity reached. Cannot register more users.");
            pause();
            return;
        }

        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = sc.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
            } else if (isUsernameTaken(username)) {
                System.out.println("Username already exists. Please choose another one.");
            } else {
                break;
            }
        }

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = sc.nextLine().trim();
            if (!Pattern.matches(EMAIL_REGEX, email)) {
                System.out.println("Invalid email format. Please enter a valid email (e.g., user@example.com).");
            } else {
                break;
            }
        }

        String password;
        while (true) {
            System.out.print("Enter password (min 8 chars, 1 uppercase, 1 lowercase, 1 digit): ");
            password = sc.nextLine();
            if (isPasswordValid(password)) {
                break;
            } else {
                System.out.println("Password does not meet the security requirements. Please try again.");
            }
        }

        System.out.print("Enter initial deposit amount: ");
        double balance = readPositiveDoubleAllowZero();

        // Create user and bank account, then link them
        User newUser = new User(username, password, email);
        BankAccount newAccount = new BankAccount(newUser, balance);
        newUser.setBankAccount(newAccount);

        // Record the initial deposit as the first transaction
        newUser.addTransaction("Initial Deposit", balance);

        users[userCount] = newUser;
        userCount++;

        System.out.println("\n✅ Registration successful!");
        pause();
    }

    public static boolean isUsernameTaken(String username) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    public static int loginUser() {
        clearScreen();
        System.out.println("--- 🔑 User Login ---");
        System.out.print("Enter username: ");
        String tempUserName = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String tempPassword = sc.nextLine();

        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equals(tempUserName) && users[i].getPassword().equals(tempPassword)) {
                return i;
            }
        }
        return -1;
    }

    /* ---------------- User Menu & Operations ---------------- */

    public static void userMenu(int userIndex) {
        while (true) {
            clearScreen();
            User u = users[userIndex];
            System.out.println("Hello, " + u.getName() + "! 😊");
            System.out.println("--- Main Menu ---");
            System.out.println("1. Deposit Money 💰");
            System.out.println("2. Withdraw Money 💸");
            System.out.println("3. Show Balance 📊");
            System.out.println("4. View Account Details & History 📜");
            System.out.println("5. Logout 🚪");
            System.out.print("Enter your choice: ");

            int choice = readIntSafe();

            switch (choice) {
                case 1 -> depositMoney(userIndex);
                case 2 -> withdrawMoney(userIndex);
                case 3 -> showBalance(userIndex);
                case 4 -> viewAccountDetails(userIndex);
                case 5 -> {
                    System.out.println("Logging out...");
                    pause();
                    return;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    pause();
                }
            }
        }
    }

    public static void depositMoney(int userIndex) {
        clearScreen();
        System.out.println("--- 💰 Deposit Money ---");
        System.out.print("Enter the amount to deposit: ");
        double amount = readPositiveDouble();
        User u = users[userIndex];

        u.getBankAccount().deposit(amount);
        u.addTransaction("Deposit", amount);

        System.out.println("\nSuccessfully deposited $" + format(amount));
        System.out.println("New balance: $" + format(u.getBankAccount().getBalance()));
        pause();
    }

    public static void withdrawMoney(int userIndex) {
        clearScreen();
        System.out.println("--- 💸 Withdraw Money ---");
        System.out.print("Enter the amount to withdraw: ");
        double amount = readPositiveDouble();
        User u = users[userIndex];

        if (u.getBankAccount().withdraw(amount)) {
            u.addTransaction("Withdrawal", amount);
            System.out.println("\nSuccessfully withdrew $" + format(amount));
            System.out.println("New balance: $" + format(u.getBankAccount().getBalance()));
        } else {
            System.out.println("\n❌ Insufficient funds. Current balance: $" + format(u.getBankAccount().getBalance()));
        }
        pause();
    }

    public static void showBalance(int userIndex) {
        clearScreen();
        User u = users[userIndex];
        System.out.println("--- 📊 Current Balance ---");
        System.out.println("Your current balance is: $" + format(u.getBankAccount().getBalance()));
        pause();
    }

    public static void viewAccountDetails(int userIndex) {
        clearScreen();
        User u = users[userIndex];
        BankAccount account = u.getBankAccount();
        System.out.println("--- 📜 Account Details ---");
        System.out.println("Username    : " + u.getName());
        System.out.println("Email       : " + u.getEmail());
        System.out.println("Account No. : " + account.getAccountNumber());
        System.out.println("Balance     : $" + format(account.getBalance()));
        System.out.println("\n--- Transaction History ---");
        if (u.getTransactionCount() == 0) {
            System.out.println("No transactions yet.");
        } else {
            for (int i = 0; i < u.getTransactionCount(); i++) {
                System.out.printf("%-16s $%10s  %s%n",
                        u.getTxnType(i),
                        format(u.getTxnAmount(i)),
                        u.getTxnTimestamp(i));
            }
        }
        pause();
    }

    /* ---------------- Utility Methods ---------------- */

    public static int readIntSafe() {
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            sc.next();
            System.out.print("Enter again: ");
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume newline
        return val;
    }

    public static double readPositiveDouble() {
        while (true) {
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble();
                sc.nextLine();
                if (v > 0) return v;
                System.out.print("Value must be positive. Try again: ");
            } else {
                System.out.print("Invalid number. Try again: ");
                sc.next();
            }
        }
    }

    public static double readPositiveDoubleAllowZero() {
        while (true) {
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble();
                sc.nextLine();
                if (v >= 0) return v;
                System.out.print("Value cannot be negative. Try again: ");
            } else {
                System.out.print("Invalid number. Try again: ");
                sc.next();
            }
        }
    }

    public static String format(double d) {
        return String.format("%.2f", d);
    }

    public static void pause() {
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

/* ---------------- BankAccount Class ---------------- */

class BankAccount {
    private final String accountNumber;
    private double balance; // Corrected typo from "doubal blance"
    private final User owner;

    public BankAccount(User owner, double initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
        // Generate a simple random 10-digit account number
        this.accountNumber = String.valueOf(ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L));
    }

    // --- Getters ---
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public User getOwner() {
        return owner;
    }

    // --- Business Logic Methods (Better Encapsulation than a public setter for balance) ---
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            return true; // Withdrawal successful
        }
        return false; // Withdrawal failed
    }
}


/* ---------------- User Class ---------------- */

class User {
    private final String userName;
    private final String password;
    private final String email;

    // Each user is now linked to one bank account
    private BankAccount bankAccount;

    // Transaction store remains with the User to track their activity
    private static final int MAX_TXN = 500;
    private final String[] txnType = new String[MAX_TXN];
    private final double[] txnAmount = new double[MAX_TXN];
    private final String[] txnTimestamp = new String[MAX_TXN];
    private int txnCount = 0;

    // Constructor updated to not include balance directly
    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    // --- Getters and Setters ---
    public String getName() { return userName; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    // --- Transaction Methods ---
    public void addTransaction(String type, double amount) {
        if (txnCount >= MAX_TXN) {
            return; // In a real system, handle this better
        }
        txnType[txnCount] = type;
        txnAmount[txnCount] = amount;
        txnTimestamp[txnCount] = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        txnCount++;
    }

    public int getTransactionCount() { return txnCount; }
    public String getTxnType(int i) { return txnType[i]; }
    public double getTxnAmount(int i) { return txnAmount[i]; }
    public String getTxnTimestamp(int i) { return txnTimestamp[i]; }
}