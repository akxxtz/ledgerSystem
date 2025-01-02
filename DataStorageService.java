import java.io.*;
import java.nio.file.*;
import java.util.*;

// Core data storage service for managing CSV files
public class DataStorageService {
    private static final String DATA_DIR = "data";
    private static final String USERS_CSV = "users.csv";
    private static final Map<String, String> CSV_HEADERS = new HashMap<>();
    private static int nextUserId = 1;

    static {
        CSV_HEADERS.put(USERS_CSV, "user_id,name,email,password,created_at,last_login");
    }

    public DataStorageService() {
        initializeStorage();
    }

    // Initialize storage system
    private void initializeStorage() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            for (Map.Entry<String, String> entry : CSV_HEADERS.entrySet()) {
                Path filePath = getFilePath(entry.getKey());
                if (!Files.exists(filePath)) {
                    Files.write(filePath, Collections.singletonList(entry.getValue()));
                }
            }
            updateNextUserId();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage", e);
        }
    }

    // Get file path for CSV file
    private Path getFilePath(String filename) {
        return Paths.get(DATA_DIR, filename);
    }

    // Update next user ID based on existing users
    private void updateNextUserId() throws IOException {
        Path userFile = getFilePath(USERS_CSV);
        if (!Files.exists(userFile)) return;

        try (BufferedReader reader = Files.newBufferedReader(userFile)) {
            reader.readLine(); // Skip header
            String line;
            int maxId = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    int id = Integer.parseInt(line.split(",")[0]);
                    maxId = Math.max(maxId, id);
                }
            }
            nextUserId = maxId + 1;
        }
    }

    // Save user to CSV
    public synchronized int saveUser(User user) throws StorageException {
        try {
            Path filePath = getFilePath(USERS_CSV);
            List<String> lines = new ArrayList<>();
            lines.add(user.toCsvString());
            Files.write(filePath, lines, StandardOpenOption.APPEND);
            return user.getUserId();
        } catch (IOException e) {
            throw new StorageException("Failed to save user: " + e.getMessage());
        }
    }

    // Load user by email
    public User loadUser(String email) throws StorageException {
        try {
            Path filePath = getFilePath(USERS_CSV);
            if (!Files.exists(filePath)) return null;

            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                reader.readLine(); // Skip header
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        User user = User.fromCsvString(line);
                        if (user.getEmail().equals(email)) {
                            return user;
                        }
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new StorageException("Failed to load user: " + e.getMessage());
        }
    }

    // Generate new user ID
    public synchronized int generateNewUserId() {
        return nextUserId++;
    }
}
