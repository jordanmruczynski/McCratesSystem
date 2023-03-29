package pl.jordii.mccratessystem.ui.items;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import pl.jordii.mccratessystem.CratesRepository;
import pl.jordii.mccratessystem.animations.scratch.ScratchMenu;
import pl.jordii.mccratessystem.database.MySQLUserService;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.util.ServerMainThread;

public class OpenCrateItem extends BaseItem {

    private Crate crate;
    private final MySQLUserService userService = CratesRepository.getMySQLUserService();

    public OpenCrateItem(Crate crate) {
        this.crate = crate;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.LIME_DYE)
                .setDisplayName(ChatColor.GREEN + "Otwórz")
                .addLoreLines("§fKliknij, aby otworzyć!");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            userService.keys(player.getUniqueId(), crate.getCrateName()).thenAccept(keys -> {
                if (keys > 0) {
                    ServerMainThread.RunParallel.run(() -> {
                        new ScratchMenu(player, crate);
                    });
                    return;
                } else {
                    player.sendMessage("§cNie posiadasz żadnych kluczy do tej skrzyni!");
                    player.closeInventory();
                }
            });
        }
    }

}
