public class User {
    private static int nextUserId = 1;

    private int userId;
    private String username;
    private String password;
    private UserRole role;

    public User(String username, String password, UserRole role) {
        this.userId = nextUserId++;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}
