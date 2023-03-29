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
import pl.jordii.mccratessystem.CratesRepository;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.ui.CrateMenu;
import pl.jordii.mccratessystem.util.ServerMainThread;

public class BackMenuItem extends BaseItem {

    private Crate crate;

    public BackMenuItem(Crate crate) {
        this.crate = crate;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.OAK_FENCE_GATE)
                .setDisplayName(ChatColor.RED + "Powrót")
                .addLoreLines("§fKliknij, aby wrócić do §bGłównego Menu§f!");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            new CrateMenu(player, crate);
        }
    }
}
