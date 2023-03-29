package pl.jordii.mccratessystem.animations.scratch;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.jordii.mccratessystem.CratesRepository;
import pl.jordii.mccratessystem.database.MySQLOpeningHistoryService;
import pl.jordii.mccratessystem.database.MySQLUserService;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.database.model.OpeningHistory;
import pl.jordii.mccratessystem.placeholderapihook.PlaceholderInject;
import pl.jordii.mccratessystem.util.CenterMessage;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ScratchItem extends BaseItem {

    private Crate crate;
    private ItemStack itemStack;
    private MySQLUserService userService = CratesRepository.getMySQLUserService();
    private MySQLOpeningHistoryService historyService = CratesRepository.getMySQLOpeningHistoryService();

    public ScratchItem(Crate crate) {
        this.crate = crate;
    }

    @Override
    public ItemProvider getItemProvider() {
        if (itemStack != null) return new ItemBuilder(itemStack);
        else return new ItemBuilder(Material.GRAY_DYE)
                .setDisplayName(ChatColor.YELLOW + "⭐");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            if (isInventoryFull(player)) {
                player.closeInventory();
                player.sendMessage("§cTwój ekwipunek jest pełny!");
                return;
            }
            if (event.getCurrentItem().getType() == Material.GRAY_DYE) {
                    userService.keys(player.getUniqueId(), crate.getCrateName()).thenAccept(keys -> {
                        if (keys > 0) {
                            keys = keys - 1;
                            userService.update(player.getUniqueId(), player.getName(), crate.getCrateName(), keys);
                            itemStack = crate.getItems().stream().filter(item -> item != null).collect(Collectors.toList()).get(ThreadLocalRandom.current().nextInt(crate.getItems().size()));
                            player.getInventory().addItem(itemStack);
                            notifyWindows();
                            historyService.create(new OpeningHistory(crate.getDisplayName(), player.getName(), player.getUniqueId(), LocalDateTime.now(), itemStack));
                            PlaceholderInject.getLastPlayerOpened().put(crate.getCrateName(), player.getName());
                            player.sendMessage("§aOtworzyłeś skrzynkę §e" + crate.getDisplayName() + "§a!");
                            sendGlobalMessageCrateOpened(player);
                        } else {
                            player.closeInventory();
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cNie posiadasz kluczy do tej skrzyni!"));
                            player.sendMessage("§cNie posiadasz kluczy do tej skrzyni!");
                            player.sendMessage("§3Klucze do skrzynek kupisz na §bwww.fommymc.net");
                        }
                    });
            }
        }
    }

    private boolean isInventoryFull(Player p) {
        return p.getInventory().firstEmpty() == -1;
    }

    private void sendGlobalMessageCrateOpened(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendMessage("");
            p.sendMessage("§fGracz §b" + player.getName() + " §fotworzył §6" + crate.getDisplayName());
            p.sendMessage("§fOtrzymał: §e" + (itemStack.getItemMeta().getDisplayName().isEmpty() ? itemStack.getType().name().toLowerCase() : itemStack.getItemMeta().getDisplayName()));
            p.sendMessage("§fTryb otwarcia: §bZdrapka");
            p.sendMessage("§3Klucze do skrzynek kupisz na §bwww.fommymc.net");
            p.sendMessage("");
        });
    }
}
