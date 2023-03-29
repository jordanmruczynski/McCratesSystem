package pl.jordii.mccratessystem.ui.items;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import pl.jordii.mccratessystem.database.model.Crate;

public class CrateSettingsItem extends BaseItem {

    private Crate crate;

    public CrateSettingsItem(Crate crate) {
        this.crate = crate;
    }

    @Override //⇒
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName(ChatColor.DARK_AQUA + "Ustawienia")
                .addLoreLines("§c#TO DO", "§7⇒ Typ csgo", "§a⇒ Typ zdrapka", "§7⇒ Brak", "", "§fKliknij, aby ustawić!");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
        }
    }
}
