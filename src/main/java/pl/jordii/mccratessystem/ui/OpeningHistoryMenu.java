package pl.jordii.mccratessystem.ui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Markers;
import de.studiocode.invui.item.Item;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.jordii.mccratessystem.CratesRepository;
import pl.jordii.mccratessystem.McCratesSystem;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.database.model.OpeningHistory;
import pl.jordii.mccratessystem.ui.items.*;
import pl.jordii.mccratessystem.util.ServerMainThread;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OpeningHistoryMenu {

    private Item border;
    private GUI gui;
    private List<Item> items;
    private Crate crate;
    private Player player;

    public OpeningHistoryMenu(Player player, Crate crate) {
        this.crate = crate;
        this.player = player;
        initializeGui();
    }

    private void initializeGui() {
        border = new SimpleItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§o"));
        CratesRepository.getMySQLOpeningHistoryService().findAll(player.getUniqueId()).thenAccept(openingHistoryList -> {
            items = openingHistoryList.stream()
                    .map(openingHistory -> new SimpleItem(new ItemBuilder(openingHistory.getDrop().getType())
                            .setDisplayName(
                                    openingHistory.getDrop().getItemMeta().getDisplayName().isEmpty() ? openingHistory.getDrop().getType().name().toLowerCase() : openingHistory.getDrop().getItemMeta().getDisplayName())
                            .addLoreLines(
                                    "",
                                    "§bData:",
                                    openingHistory.getOpeningDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                    "§bSkrzynka:",
                                    openingHistory.getCrateName())))
                    .collect(Collectors.toList());
            gui = new GUIBuilder<>(GUIType.PAGED_ITEMS)
                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . . . #",
                            "# . . . . . . . #",
                            "# . . . . . . . #",
                            "# . . . . . . . #",
                            "# < > # P # # # #")
                    .addIngredient('#', border)
                    .addIngredient('.', Markers.ITEM_LIST_SLOT_HORIZONTAL)
                    .addIngredient('<', new BackItem())
                    .addIngredient('>', new ForwardItem())
                    .addIngredient('P', new BackMenuItem(crate))
                    .setItems(items)
                    .build();
            player.sendMessage(items.size() + "");
            ServerMainThread.RunParallel.run(() -> {
                new SimpleWindow(player, "§3Historia dropów §7| §f" + player.getName(), gui).show();
            });
        });
    }
}
