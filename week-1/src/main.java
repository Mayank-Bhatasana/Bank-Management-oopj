import java.util.Scanner;

class Main {
    static String[] userName = new String[100];
    static String[] password = new String[100];
    static double[] balance = new double[100];
    static int userCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            clearScreen();
            //greet the user
            System.out.println("Enter number: ");
            System.out.println("1) Register User");
            System.out.println("2) Login User");
            System.out.println("3) Exit");
            int n = sc.nextInt();
            if (n == 3) {
                clearScreen();
                return;
            }

            if (n == 1) {
                registerUser();
            }

            if (n == 2) {
                while (true) {
                    int userCo = loginUser();
                    //if the ans is -1 then it's not a valid user tell the user to try again
                    if (userCo == -1)
                        System.out.println("Wrong Username or password :( \ntry again!!");
                    else {
                        clearScreen();
                        displayUserInfo(userCo);
                        break;
                    }
                }
            }
        }
    }

    public static void displayUserInfo(int userCo) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Username: " + userName[userCo]);
        System.out.println("Password: " + password[userCo]);
        System.out.println("Balance: " + balance[userCo]);
        sc.nextLine();
    }

    public static void registerUser() {
        clearScreen();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username: ");
        userName[userCount] = sc.nextLine();
        System.out.println("Enter password: ");
        password[userCount] = sc.nextLine();
        System.out.println("Enter balance: ");
        balance[userCount] = sc.nextDouble();
        userCount++;
    }

    public static int loginUser() {
        clearScreen();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username: ");
        String tempUserName = sc.nextLine();
        System.out.println("Enter password: ");
        String tempPassword = sc.nextLine();
        System.out.println("The user count is :-" + userCount);
        for (int i = 0; i <= userCount; i++)
            if (tempUserName.equals(userName[i]) && tempPassword.equals(password[i])) return i;
        return -1;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

