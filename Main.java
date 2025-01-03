import java.util.Scanner;

public class Main {
    private static final UserAuthenticationService authService = new UserAuthenticationService();
    private static final Scanner scanner = new Scanner(System.in);
    private static String currentSession = null;

    public static void main(String[] args) {
        while (true) {
            if (currentSession == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n=== Login or Register ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("> ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleLogin();
                break;
            case "2":
                handleRegistration();
                break;
            case "3":
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void handleLogin() {
        System.out.println("\n=== Please enter your email and password ===");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            currentSession = authService.loginUser(email, password);
            User user = authService.getCurrentUser(currentSession);
            System.out.println("Login Successful!!!");
            System.out.println("Welcome, " + user.getName() + "!");
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void handleRegistration() {
        System.out.println("\n=== Please fill in the form ===");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine();

        try {
            currentSession = authService.registerUser(name, email, password, confirmPassword);
            System.out.println("Register Successful!!!");
        } catch (AuthenticationException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void showMainMenu() {
        User currentUser = authService.getCurrentUser(currentSession);
        if (currentUser == null) {
            currentSession = null;
            return;
        }

        System.out.println("\n=== Welcome, " + currentUser.getName() + " ===");
        System.out.println("1. Logout");
        System.out.println("2. Exit");
        System.out.print("> ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleLogout();
                break;
            case "2":
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void handleLogout() {
        authService.logoutUser(currentSession);
        currentSession = null;
        System.out.println("Logout successful!");
    }
}