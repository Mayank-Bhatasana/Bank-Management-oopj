import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Main {
    // 2D array for user data: [0]username, [1]password, [2]balance, [3]email
    static String[][] userDetails = new String[100][4];
    // 2D array for transaction history: [userIndex][type][amount][timestamp]
    static String[][] transactionHistory = new String[1000][4];
    static int userCount = 0;
    static int transactionCount = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            clearScreen();
            System.out.println("🏦 Welcome to the Bank Management System! 🏦");
            System.out.println("1. Register User");
            System.out.println("2. Login User");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // discard
                System.out.print("Enter your choice: ");
            }
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    int loggedInUserIndex = loginUser();
                    if (loggedInUserIndex != -1) {
                        userMenu(loggedInUserIndex);
                    } else {
                        System.out.println("❌ Wrong username or password. Please try again.");
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                    }
                    break;
                case 3:
                    clearScreen();
                    System.out.println("Thank you for using the Bank Management System. Goodbye! 👋");
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    System.out.println("Press Enter to continue...");
                    sc.nextLine();
            }
        }
    }

    /**
     * Registers a new user with validation for username, email, and password.
     */
    public static void registerUser() {
        clearScreen();
        System.out.println("--- 📝 User Registration ---");
        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = sc.nextLine().trim(); // Input trimming
            if (isUsernameTaken(username)) {
                System.out.println("Username already exists. Please choose another one.");
            } else {
                break;
            }
        }

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = sc.nextLine().trim(); // Input trimming
            if (!(email.contains("@") && email.contains("."))) { // Email validation
                System.out.println("Invalid email format. Please make sure to include '@' and '.'.");
            } else {
                break;
            }
        }

        String password;
        // Simplified password validation
        do {
            System.out.print("Enter password (min 8 chars, 1 uppercase, 1 lowercase, 1 digit): ");
            password = sc.nextLine();
        } while (!isPasswordValid(password));

        System.out.print("Enter initial deposit amount: ");
        double balance = 0;
        while (true) {
            if (sc.hasNextDouble()) {
                balance = sc.nextDouble();
                if (balance >= 0) break;
                else System.out.println("Initial deposit cannot be negative.");
            } else {
                System.out.println("Invalid input. Please enter a numerical value.");
                sc.next(); // Clear invalid input
            }
            System.out.print("Enter initial deposit amount: ");
        }
        sc.nextLine(); // Consume newline

        // Store new user data
        userDetails[userCount][0] = username;
        userDetails[userCount][1] = password;
        userDetails[userCount][2] = String.valueOf(balance);
        userDetails[userCount][3] = email;
        userCount++;

        System.out.println("\n✅ Registration successful!");
        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

    /**
     * Checks if a given username is already in use (case-insensitive).
     * @param username The username to check.
     * @return True if the username is taken, false otherwise.
     */
    public static boolean isUsernameTaken(String username) {
        for (int i = 0; i < userCount; i++) {
            if (userDetails[i][0].equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A very simple password validator without using high-level concepts.
     * @param password The password to validate.
     * @return True if the password is valid, false otherwise.
     */
    public static boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        // Loop through each character of the password
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
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

        // If all checks pass, the password is valid
        return true;
    }

    /**
     * Logs in a user by verifying their username and password.
     * @return The index of the user if login is successful, otherwise -1.
     */
    public static int loginUser() {
        clearScreen();
        System.out.println("--- 🔑 User Login ---");
        System.out.print("Enter username: ");
        String tempUserName = sc.nextLine().trim(); // Input trimming
        System.out.print("Enter password: ");
        String tempPassword = sc.nextLine();

        for (int i = 0; i < userCount; i++) {
            if (userDetails[i][0].equals(tempUserName) && userDetails[i][1].equals(tempPassword)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Displays the account details and transaction history of the logged-in user.
     * @param userIndex The index of the logged-in user.
     */
    public static void viewAccountDetails(int userIndex) {
        clearScreen();
        System.out.println("--- 📜 Account Details ---");
        System.out.println("Username: " + userDetails[userIndex][0]);
        System.out.println("Email: " + userDetails[userIndex][3]);
        System.out.println("Balance: $" + String.format("%.2f", Double.parseDouble(userDetails[userIndex][2])));

        System.out.println("\n--- Transaction History ---");
        boolean hasTransactions = false;
        for (int i = 0; i < transactionCount; i++) {
            if (Integer.parseInt(transactionHistory[i][0]) == userIndex) {
                System.out.printf("Type: %s, Amount: $%.2f, Date: %s%n",
                        transactionHistory[i][1],
                        Double.parseDouble(transactionHistory[i][2]),
                        transactionHistory[i][3]);
                hasTransactions = true;
            }
        }
        if (!hasTransactions) {
            System.out.println("No transactions found.");
        }
    }

    // --- Unchanged Methods from Previous Version ---

    public static void userMenu(int userIndex) {
        while (true) {
            clearScreen();
            System.out.println("Hello, " + userDetails[userIndex][0] + "! 😊");
            System.out.println("--- Main Menu ---");
            System.out.println("1. Deposit Money 💰");
            System.out.println("2. Withdraw Money 💸");
            System.out.println("3. Show Balance 📊");
            System.out.println("4. View Account Details & History 📜");
            System.out.println("5. Logout 🚪");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
                System.out.print("Enter your choice: ");
            }
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1: depositMoney(userIndex); break;
                case 2: withdrawMoney(userIndex); break;
                case 3: showBalance(userIndex); break;
                case 4: viewAccountDetails(userIndex); break;
                case 5: System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\nPress Enter to return to the menu...");
            sc.nextLine();
        }
    }

    public static void depositMoney(int userIndex) {
        clearScreen();
        System.out.println("--- 💰 Deposit Money ---");
        System.out.print("Enter the amount to deposit: ");
        double amount = 0;
        while (true) {
            if (sc.hasNextDouble()) {
                amount = sc.nextDouble();
                if (amount > 0) break;
                else System.out.println("Deposit amount must be positive.");
            } else {
                System.out.println("Invalid input. Please enter a numerical value.");
                sc.next();
            }
            System.out.print("Enter the amount to deposit: ");
        }
        sc.nextLine();

        double currentBalance = Double.parseDouble(userDetails[userIndex][2]);
        currentBalance += amount;
        userDetails[userIndex][2] = String.valueOf(currentBalance);
        addTransaction(userIndex, "Deposit", amount);
        System.out.println("\nSuccessfully deposited $" + String.format("%.2f", amount));
        System.out.println("New balance: $" + String.format("%.2f", currentBalance));
    }

    public static void withdrawMoney(int userIndex) {
        clearScreen();
        System.out.println("--- 💸 Withdraw Money ---");
        System.out.print("Enter the amount to withdraw: ");
        double amount = 0;
        while (true) {
            if (sc.hasNextDouble()) {
                amount = sc.nextDouble();
                if (amount > 0) break;
                else System.out.println("Withdrawal amount must be positive.");
            } else {
                System.out.println("Invalid input. Please enter a numerical value.");
                sc.next();
            }
            System.out.print("Enter the amount to withdraw: ");
        }
        sc.nextLine();

        double currentBalance = Double.parseDouble(userDetails[userIndex][2]);
        if (amount > currentBalance) {
            System.out.println("\n❌ Insufficient funds. Your current balance is $" + String.format("%.2f", currentBalance));
        } else {
            currentBalance -= amount;
            userDetails[userIndex][2] = String.valueOf(currentBalance);
            addTransaction(userIndex, "Withdrawal", amount);
            System.out.println("\nSuccessfully withdrew $" + String.format("%.2f", amount));
            System.out.println("New balance: $" + String.format("%.2f", currentBalance));
        }
    }

    public static void showBalance(int userIndex) {
        clearScreen();
        System.out.println("--- 📊 Current Balance ---");
        System.out.println("Your current balance is: $" + String.format("%.2f", Double.parseDouble(userDetails[userIndex][2])));
    }

    public static void addTransaction(int userIndex, String type, double amount) {
        transactionHistory[transactionCount][0] = String.valueOf(userIndex);
        transactionHistory[transactionCount][1] = type;
        transactionHistory[transactionCount][2] = String.valueOf(amount);
        transactionHistory[transactionCount][3] = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        transactionCount++;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}