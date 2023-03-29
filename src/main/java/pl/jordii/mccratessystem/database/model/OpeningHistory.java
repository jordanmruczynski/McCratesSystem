package pl.jordii.mccratessystem.database.model;

import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.UUID;

public class OpeningHistory {

    private String crateName;
    private String playerName;
    private UUID uuid;
    private LocalDateTime openingDate;
    private ItemStack drop;

    public OpeningHistory(String crateName, String playerName, UUID uuid, LocalDateTime openingDate, ItemStack drop) {
        this.crateName = crateName;
        this.playerName = playerName;
        this.uuid = uuid;
        this.openingDate = openingDate;
        this.drop = drop;
    }

    public String getCrateName() {
        return crateName;
    }

    public void setCrateName(String crateName) {
        this.crateName = crateName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public void setDrop(ItemStack drop) {
        this.drop = drop;
    }
}
