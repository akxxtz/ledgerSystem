public class Main {
    public static void main(String[] args) {
        UserAuthenticationService authService = new UserAuthenticationService();

        try {
            // Register new user
            String sessionToken = authService.registerUser(
                    "John Doe",
                    "john@example.com",
                    "Pass123!",
                    "Pass123!"
            );
            System.out.println("Registration successful! Session: " + sessionToken);

            // Login
            sessionToken = authService.loginUser("john@example.com", "Pass123!");
            System.out.println("Login successful! Session: " + sessionToken);

            // Get current user
            User currentUser = authService.getCurrentUser(sessionToken);
            System.out.println("Current user: " + currentUser.getName());

            // Logout
            authService.logoutUser(sessionToken);
            System.out.println("Logout successful!");

        } catch (AuthenticationException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
    }
}
