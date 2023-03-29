package pl.jordii.mccratessystem.ui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Markers;
import de.studiocode.invui.item.Item;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.jordii.mccratessystem.CratesRepository;
import pl.jordii.mccratessystem.database.MySQLCrateService;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.ui.items.*;

import java.util.List;
import java.util.stream.Collectors;

public class CrateMenu {

    private Item border;
    private GUI gui;
    private List<Item> items;
    private Crate crate;

    public CrateMenu(Player player, Crate crate) {
        this.crate = crate;
        initializeGui();
        new SimpleWindow(player, "§3Skrzynki §f| " + crate.getDisplayName(), gui).show();
    }

    private void initializeGui() {
        border = new SimpleItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§r"));

        items = crate.getItems().stream()
                .filter(is -> is != null)
                .map(is -> new SimpleItem(new ItemBuilder(is)))
                .collect(Collectors.toList());

        gui = new GUIBuilder<>(GUIType.PAGED_ITEMS)
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# < > # O # U H #")
                .addIngredient('#', border)
                .addIngredient('.', Markers.ITEM_LIST_SLOT_HORIZONTAL)
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .addIngredient('O', new OpenCrateItem(crate))
                .addIngredient('U', new CrateSettingsItem(crate))
                .addIngredient('H', new OpenHistoryItem(crate))
                .setItems(items)
                .build();

    }
}
