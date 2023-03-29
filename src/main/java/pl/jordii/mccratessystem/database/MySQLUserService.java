package pl.jordii.mccratessystem.database;

import pl.jordii.mccratessystem.database.mysql.MySQLConnection;
import pl.jordii.mccratessystem.database.services.UserService;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MySQLUserService implements UserService {

    private final MySQLConnection connection;

    public MySQLUserService(MySQLConnection mySQLConnection) {
        this.connection = mySQLConnection;
    }

    public CompletableFuture<Void> createTable() {
        final String sql = "CREATE TABLE IF NOT EXISTS crates_keys (id INT NOT NULL UNIQUE PRIMARY KEY AUTO_INCREMENT, uuid VARCHAR(50) NOT NULL, player_name VARCHAR(50) NOT NULL, crate_name VARCHAR(100) NOT NULL, keys_amount INT NOT NULL, FOREIGN KEY (crate_name) REFERENCES crates(crate_name))";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); Statement statement = conn.createStatement()) {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Integer> keys(UUID uuid, String crateName) {
        final String sql = "SELECT * FROM crates_keys WHERE uuid = ? AND crate_name = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, crateName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("keys_amount");
                    }
                };
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    @Override
    public CompletableFuture<Void> update(UUID uuid, String playerName, String crateName, int keys) {
        final String sql = "UPDATE crates_keys SET keys_amount = ? WHERE uuid = ? AND crate_name = ?";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, keys);
                stmt.setString(2, uuid.toString());
                stmt.setString(3, crateName);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> save(UUID uuid, String playerName, String crateName, int keys) {
        final String sql = "INSERT INTO crates_keys(uuid, player_name, crate_name, keys_amount) VALUES (?, ?, ?, ?)";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, playerName);
                stmt.setString(3, crateName);
                stmt.setInt(4, keys);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> delete(UUID uuid, String crateName) {
        final String sql = "DELETE FROM crates_keys WHERE uuid = ? AND crate_name = ?";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, crateName);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
