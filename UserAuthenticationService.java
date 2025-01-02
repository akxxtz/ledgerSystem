import java.time.LocalDateTime;

// Main authentication service
public class UserAuthenticationService {
    private final DataStorageService storageService;
    private final SessionManager sessionManager;

    public UserAuthenticationService() {
        this.storageService = new DataStorageService();
        this.sessionManager = new SessionManager();
    }

    // Validate email format
    public boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    // Validate password complexity
    public boolean validatePassword(String password) {
        if (password == null || password.length() < 6) return false;
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        return hasNumber && hasLetter && hasSpecial;
    }

    // Validate name format
    public boolean validateName(String name) {
        return name != null && name.matches("^[a-zA-Z0-9 ]+$");
    }

    // Register new user
    public String registerUser(String name, String email, String password, String confirmPassword)
            throws AuthenticationException {
        // Validate inputs
        if (!validateName(name)) {
            throw new AuthenticationException("Invalid name format");
        }
        if (!validateEmail(email)) {
            throw new AuthenticationException("Invalid email format");
        }
        if (!validatePassword(password)) {
            throw new AuthenticationException("Password does not meet complexity requirements");
        }
        if (!password.equals(confirmPassword)) {
            throw new AuthenticationException("Passwords do not match");
        }

        try {
            // Check if user already exists
            if (storageService.loadUser(email) != null) {
                throw new AuthenticationException("Email already registered");
            }

            // Create and save new user
            int userId = storageService.generateNewUserId();
            User newUser = new User(userId, name, email, password); // In production, hash the password
            storageService.saveUser(newUser);

            // Create session for new user
            return sessionManager.createSession(newUser);
        } catch (StorageException e) {
            throw new AuthenticationException("Registration failed: " + e.getMessage());
        }
    }

    // Login user
    public String loginUser(String email, String password) throws AuthenticationException {
        try {
            User user = storageService.loadUser(email);
            if (user == null) {
                throw new AuthenticationException("Invalid email or password");
            }

            // In production, verify hashed password
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationException("Invalid email or password");
            }

            // Update last login and create session
            user.setLastLogin(LocalDateTime.now());
            return sessionManager.createSession(user);
        } catch (StorageException e) {
            throw new AuthenticationException("Login failed: " + e.getMessage());
        }
    }

    // Get current user from session
    public User getCurrentUser(String sessionToken) {
        return sessionManager.getUserFromSession(sessionToken);
    }

    // Logout user
    public void logoutUser(String sessionToken) {
        sessionManager.endSession(sessionToken);
    }
}
