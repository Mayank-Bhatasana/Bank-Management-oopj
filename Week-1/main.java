import java.util.Scanner;

class main {
    static String[] userName = new String[100];
    static String[] password = new String[100];
    static double[] balance = new double[100];
    static int userCount = 0;

    public static void main(String args[]) {
        clearScreen();

        Scanner sc = new Scanner(System.in);
        while (true) {

            //greet the user
            System.out.println("Enter number: ");
            System.out.println("1) Register User");
            System.out.println("2) Login User");
            System.out.println("3) Exit");
            int n = sc.nextInt();
            if (n == 3) return;
            if (n == 2) {
                while (true) {

                    int userCo = loginUser();
                    //if the ans is -1 then it's not a valid user tell the user to try again
                    if (userCo == -1) System.out.println("Wrong User:( \ntry again!!");
                    else {
                        System.out.println("Username: " + userName[userCo]);
                        System.out.println("Password: " + password[userCo]);
                        System.out.println("Balance: " + balance[userCo]);
                    }
                }
            }
            if (n == 1) {
                registerUser();
            }
        }
    }

    public static void registerUser() {
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
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username: ");
        String tempUserName = sc.nextLine();
        System.out.println("Enter password: ");
        String tempPassword = sc.nextLine();
        System.out.println("The user count is :-" + userCount);
        for (int i = 0; i <= userCount; i++)
            if (userName[i] == tempUserName && password[i] == tempPassword) return i;

        return -1;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

