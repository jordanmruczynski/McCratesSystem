package pl.jordii.mccratessystem.database;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.database.model.OpeningHistory;
import pl.jordii.mccratessystem.database.model.ParticlesAnimationType;
import pl.jordii.mccratessystem.database.mysql.MySQLConnection;
import pl.jordii.mccratessystem.database.services.CrateService;
import pl.jordii.mccratessystem.database.services.HistoryService;
import pl.jordii.mccratessystem.util.LoadoutConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class MySQLOpeningHistoryService implements HistoryService {

    private final MySQLConnection connection;

    public MySQLOpeningHistoryService(MySQLConnection connection ) {
        this.connection = connection;
    }

    public CompletableFuture<Void> createTable() {
        final String sql = "CREATE TABLE IF NOT EXISTS opening_history (id INT NOT NULL UNIQUE PRIMARY KEY AUTO_INCREMENT, uuid VARCHAR(50) NOT NULL, player_name VARCHAR(100) NOT NULL, crate_name VARCHAR(200) NOT NULL, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, item TEXT NOT NULL)";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); Statement statement = conn.createStatement()) {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<List<OpeningHistory>> findAll(UUID uuid) {
        final String sql = "SELECT * FROM opening_history WHERE uuid = ? ORDER BY created_at DESC";
        return CompletableFuture.supplyAsync(() -> {
           final List<OpeningHistory> openingHistories = Lists.newArrayList();
           try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1,uuid.toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Bukkit.getLogger().log(Level.INFO, "addodano");
                        openingHistories.add(new OpeningHistory(rs.getString("crate_name"), rs.getString("player_name"), uuid, LocalDateTime.parse(rs.getString("created_at"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), LoadoutConverter.decodeLoadoutSingleItem(rs.getString("item"))));
                    }
                }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return openingHistories;
        });
    }

    @Override
    public CompletableFuture<Void> create(OpeningHistory crate) {
        final String sql = "INSERT INTO opening_history (uuid, player_name, crate_name, item) VALUES (?, ?, ?, ?)";
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, crate.getUuid().toString());
                stmt.setString(2, crate.getPlayerName());
                stmt.setString(3, crate.getCrateName());
                stmt.setString(4, LoadoutConverter.encodeLoadoutSingleItem(crate.getDrop()));
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
