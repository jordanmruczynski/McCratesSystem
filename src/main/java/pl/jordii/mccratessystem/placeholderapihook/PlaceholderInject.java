package pl.jordii.mccratessystem.placeholderapihook;

import com.google.common.collect.Maps;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlaceholderInject extends PlaceholderExpansion {

    private static Map<String, String> lastPlayerOpened = Maps.newHashMap();

    @Override
    public String getIdentifier() {
        return "mccratessystem";
    }

    @Override
    public String getAuthor() {
        return "JordanM";
    }

    @Override
    public String getVersion() {
        return "1.1";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.startsWith("lastOpenedBy_")) {
            String crateName = identifier.replace("lastOpenedBy_", "");
            return lastPlayerOpened.get(crateName) == null ? "Brak" : lastPlayerOpened.get(crateName);
        }
        return null;
    }

    public static Map<String, String> getLastPlayerOpened() {
        return lastPlayerOpened;
    }
}

