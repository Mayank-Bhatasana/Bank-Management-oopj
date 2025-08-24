import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Main {

    static int userCount = 0;
    static final int MAX_USERS = 100;
    static final Scanner sc = new Scanner(System.in);
    static final User[] users = new User[MAX_USERS];

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
            if (!(email.contains("@") && email.contains("."))) {
                System.out.println("Invalid email format. Please include '@' and '.'.");
            } else {
                break;
            }
        }

        String password;
        do {
            System.out.print("Enter password (min 8 chars, 1 uppercase, 1 lowercase, 1 digit): ");
            password = sc.nextLine();
        } while (!isPasswordValid(password));

        System.out.print("Enter initial deposit amount: ");
        double balance = readPositiveDoubleAllowZero();

        users[userCount] = new User(username, password, email, balance);
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
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasUpper) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }
        if (!hasLower) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }
        if (!hasDigit) {
            System.out.println("Password must contain at least one digit.");
            return false;
        }
        return true;
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
        u.setBalance(u.getBalance() + amount);
        u.addTransaction("Deposit", amount);
        System.out.println("\nSuccessfully deposited $" + format(amount));
        System.out.println("New balance: $" + format(u.getBalance()));
        pause();
    }

    public static void withdrawMoney(int userIndex) {
        clearScreen();
        System.out.println("--- 💸 Withdraw Money ---");
        System.out.print("Enter the amount to withdraw: ");
        double amount = readPositiveDouble();
        User u = users[userIndex];

        if (amount > u.getBalance()) {
            System.out.println("\n❌ Insufficient funds. Current balance: $" + format(u.getBalance()));
        } else {
            u.setBalance(u.getBalance() - amount);
            u.addTransaction("Withdrawal", amount);
            System.out.println("\nSuccessfully withdrew $" + format(amount));
            System.out.println("New balance: $" + format(u.getBalance()));
        }
        pause();
    }

    public static void showBalance(int userIndex) {
        clearScreen();
        User u = users[userIndex];
        System.out.println("--- 📊 Current Balance ---");
        System.out.println("Your current balance is: $" + format(u.getBalance()));
        pause();
    }

    public static void viewAccountDetails(int userIndex) {
        clearScreen();
        User u = users[userIndex];
        System.out.println("--- 📜 Account Details ---");
        System.out.println("Username : " + u.getName());
        System.out.println("Email    : " + u.getEmail());
        System.out.println("Balance  : $" + format(u.getBalance()));
        System.out.println("\n--- Transaction History ---");
        if (u.getTransactionCount() == 0) {
            System.out.println("No transactions yet.");
        } else {
            for (int i = 0; i < u.getTransactionCount(); i++) {
                System.out.printf("%-10s $%10s  %s%n",
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
        // Works in many terminals; harmless otherwise.
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

/* ---------------- User Class ---------------- */

class User {
    private final String userName;
    private final String password;
    private final String email;
    private double balance;

    // Simple fixed-size transaction store
    private static final int MAX_TXN = 500;
    private final String[] txnType = new String[MAX_TXN];
    private final double[] txnAmount = new double[MAX_TXN];
    private final String[] txnTimestamp = new String[MAX_TXN];
    private int txnCount = 0;

    public User(String userName, String password, String email, double balance) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.balance = balance;
        addTransaction("Initial", balance);
    }

    public String getName() { return userName; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public void addTransaction(String type, double amount) {
        if (txnCount >= MAX_TXN) {
            // In a real system you'd expand or persist.
            return;
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