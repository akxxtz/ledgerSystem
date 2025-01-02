import java.time.LocalDateTime;
// User class representing the core user entity
public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public User(int userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    // Convert user to CSV format
    public String toCsvString() {
        return String.format("%d,%s,%s,%s,%s,%s",
                userId, name, email, password,
                createdAt.toString(),
                lastLogin != null ? lastLogin.toString() : ""
        );
    }

    // Create user from CSV line
    public static User fromCsvString(String line) {
        String[] parts = line.split(",");
        User user = new User(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                parts[3]
        );
        user.createdAt = LocalDateTime.parse(parts[4]);
        if (parts.length > 5 && !parts[5].isEmpty()) {
            user.lastLogin = LocalDateTime.parse(parts[5]);
        }
        return user;
    }
}