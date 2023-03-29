package pl.jordii.mccratessystem.database;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.jordii.mccratessystem.database.mysql.MySQLConnection;
import pl.jordii.mccratessystem.database.services.LocationService;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class MySQLLocationService implements LocationService {

    private final MySQLConnection connection;

    public MySQLLocationService(MySQLConnection mySQLConnection) {
        this.connection = mySQLConnection;

    }

    public void createTable() throws SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS crates_locations (id INT NOT NULL UNIQUE PRIMARY KEY AUTO_INCREMENT, crate_name VARCHAR(100) NOT NULL, world_name VARCHAR(100) NOT NULL, x DOUBLE NOT NULL, y DOUBLE NOT NULL, z DOUBLE NOT NULL, FOREIGN KEY (crate_name) REFERENCES crates(crate_name))";
        try (Connection conn = connection.getConnection(); Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
    }


    @Override
    public CompletableFuture<String> find(Location location) {
        final String sql = "SELECT * FROM crates_locations WHERE world_name = ? AND x = ? AND y = ? AND z = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, location.getWorld().getName());
                stmt.setDouble(2, location.getX());
                stmt.setDouble(3, location.getY());
                stmt.setDouble(4, location.getZ());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("crate_name");
                    }
                };
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Map<Location, Map<Integer, String>>> findAllWithIds() {
        final String sql = "SELECT * FROM crates_locations";
        return CompletableFuture.supplyAsync(() -> {
            final Map<Location, Map<Integer, String>> locations = Maps.newHashMap();
            try (Connection conn = connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    locations.put(new Location(Bukkit.getWorld(rs.getString("world_name")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z")), ImmutableMap.of(rs.getInt("id"), rs.getString("crate_name")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return locations;
        });
    }

    @Override
    public CompletableFuture<Map<Location, String>> findAll() {
        final String sql = "SELECT * FROM crates_locations";
        return CompletableFuture.supplyAsync(() -> {
            final Map<Location, String> locations = Maps.newHashMap();
            try (Connection conn = connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    locations.put(new Location(Bukkit.getWorld(rs.getString("world_name")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z")), rs.getString("crate_name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return locations;
        });
    }

    @Override
    public CompletableFuture<Void> save(String crateName, Location location) {
        final String sql = "INSERT INTO crates_locations(crate_name, world_name, x, y, z) VALUES (?, ?, ?, ?, ?)";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, crateName);
                stmt.setString(2, location.getWorld().getName());
                stmt.setDouble(3, location.getX());
                stmt.setDouble(4, location.getY());
                stmt.setDouble(5, location.getZ());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> delete(String crateName, Location location) {
        return null;
    }

    @Override
    public CompletableFuture<Void> delete(int id) {
        final String sql = "DELETE FROM crates_locations WHERE id = ?";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll(String crateName) {
        return null;
    }
}
