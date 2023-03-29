package pl.jordii.mccratessystem.database;

import com.google.common.collect.Lists;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.database.model.ParticlesAnimationType;
import pl.jordii.mccratessystem.database.mysql.MySQLConnection;
import pl.jordii.mccratessystem.database.services.CrateService;
import pl.jordii.mccratessystem.util.LoadoutConverter;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class MySQLCrateService implements CrateService {

    private final MySQLConnection connection;

    public MySQLCrateService(MySQLConnection connection ) {
        this.connection = connection;
    }

    public CompletableFuture<Void> createTable() {
        final String sql = "CREATE TABLE IF NOT EXISTS crates (crate_name VARCHAR(100) NOT NULL PRIMARY KEY, crate_displayname VARCHAR(200) NOT NULL, items TEXT, status BOOL NOT NULL, particles_animation_type VARCHAR(50) NOT NULL, particles_color VARCHAR(30) NOT NULL)";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); Statement statement = conn.createStatement()) {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<List<Crate>> findAll() {
        final String sql = "SELECT * FROM crates";
        return CompletableFuture.supplyAsync(() -> {
            final List<Crate> crates = Lists.newArrayList();
            try (Connection conn = connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    crates.add(new Crate(rs.getString("crate_name"), rs.getString("crate_displayname"), LoadoutConverter.decodeLoadout(rs.getString("items")), rs.getBoolean("status"), ParticlesAnimationType.valueOf(rs.getString("particles_animation_type")), rs.getString("particles_color")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return crates;
        });
    }

    @Override
    public CompletableFuture<Crate> find(String crateName) {
        final String sql = "SELECT * FROM crates WHERE crate_name = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, crateName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Crate(rs.getString("crate_name"), rs.getString("crate_displayname"), LoadoutConverter.decodeLoadout(rs.getString("items")), rs.getBoolean("status"), ParticlesAnimationType.valueOf(rs.getString("particles_animation_type")),rs.getString("particles_color"));
                    }
                };
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }


    @Override
    public CompletableFuture<Void> create(Crate crate) {
        final String sql = "INSERT INTO crates(crate_name, crate_displayname, items, status, particles_animation_type, particles_color) VALUES (?, ?, ?, ?, ?, ?)";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, crate.getCrateName());
                stmt.setString(2, crate.getDisplayName());
                stmt.setString(3, LoadoutConverter.encodeLoadout(crate.getItems()));
                stmt.setBoolean(4, crate.isStatus());
                stmt.setString(5, crate.getParticlesAnimationType().toString());
                stmt.setString(6, crate.getParticlesColor());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> updateStatus(String crateName, boolean status) {
        final String sql = "UPDATE crates SET status = ? WHERE crate_name = ?";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setBoolean(1, status);
                stmt.setString(2, crateName);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> delete(String crateName) {
        final String sql = "DELETE FROM crates WHERE crate_name = ?";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, crateName);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> update(Crate crate) {
        final String sql = "UPDATE crates SET crate_displayname = ?, items = ? WHERE crate_name = ?";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, crate.getDisplayName());
                stmt.setString(2, LoadoutConverter.encodeLoadout(crate.getItems()));
                stmt.setString(3, crate.getCrateName());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
