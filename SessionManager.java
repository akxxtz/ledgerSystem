import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Session management for keeping track of logged-in users
public class SessionManager {
    private static final int SESSION_TIMEOUT_MINUTES = 30;
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    private class SessionInfo {
        User user;
        LocalDateTime lastAccessed;

        SessionInfo(User user) {
            this.user = user;
            this.lastAccessed = LocalDateTime.now();
        }
    }

    // Generate a new session token
    private String generateSessionToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    // Create new session for user
    public String createSession(User user) {
        String token = generateSessionToken();
        sessions.put(token, new SessionInfo(user));
        return token;
    }

    // Get user from session token
    public User getUserFromSession(String token) {
        SessionInfo session = sessions.get(token);
        if (session == null) return null;

        if (LocalDateTime.now().minusMinutes(SESSION_TIMEOUT_MINUTES)
                .isAfter(session.lastAccessed)) {
            sessions.remove(token);
            return null;
        }

        session.lastAccessed = LocalDateTime.now();
        return session.user;
    }

    // End user session
    public void endSession(String token) {
        sessions.remove(token);
    }

    // Clean up expired sessions
    public void cleanupSessions() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(SESSION_TIMEOUT_MINUTES);
        sessions.entrySet().removeIf(entry ->
                entry.getValue().lastAccessed.isBefore(cutoff));
    }
}

