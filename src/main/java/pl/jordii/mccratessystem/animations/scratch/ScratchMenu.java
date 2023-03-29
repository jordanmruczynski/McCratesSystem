package pl.jordii.mccratessystem.animations.scratch;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.item.Item;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.jordii.mccratessystem.CratesRepository;
import pl.jordii.mccratessystem.database.MySQLCrateService;
import pl.jordii.mccratessystem.database.MySQLUserService;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.ui.items.*;

import java.util.List;

public class ScratchMenu {

    private Item border;
    private GUI gui;
    private Crate crate;

    public ScratchMenu(Player player, Crate crate) {
        this.crate = crate;
        initializeGui();
        new SimpleWindow(player, "§6Otwieranie.. §8| §e"+ crate.getDisplayName(), gui).show();
    }

    private void initializeGui() {
        border = new SimpleItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§r"));

        gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(
                        "# # # # # # # # #",
                        "# 1 2 3 4 5 6 7 #",
                        "# 8 9 a b c d e #",
                        "# f g h i j k l #",
                        "# # # # P # # # #")
                .addIngredient('#', border)
                .addIngredient('.', new ScratchItem(crate))
                .addIngredient('1', new ScratchItem(crate))
                .addIngredient('2', new ScratchItem(crate))
                .addIngredient('3', new ScratchItem(crate))
                .addIngredient('4', new ScratchItem(crate))
                .addIngredient('5', new ScratchItem(crate))
                .addIngredient('6', new ScratchItem(crate))
                .addIngredient('7', new ScratchItem(crate))
                .addIngredient('8', new ScratchItem(crate))
                .addIngredient('9', new ScratchItem(crate))
                .addIngredient('a', new ScratchItem(crate))
                .addIngredient('b', new ScratchItem(crate))
                .addIngredient('c', new ScratchItem(crate))
                .addIngredient('d', new ScratchItem(crate))
                .addIngredient('e', new ScratchItem(crate))
                .addIngredient('f', new ScratchItem(crate))
                .addIngredient('g', new ScratchItem(crate))
                .addIngredient('h', new ScratchItem(crate))
                .addIngredient('i', new ScratchItem(crate))
                .addIngredient('j', new ScratchItem(crate))
                .addIngredient('k', new ScratchItem(crate))
                .addIngredient('l', new ScratchItem(crate))
                .addIngredient('P', new BackMenuItem(crate))
                .build();

    }
}
