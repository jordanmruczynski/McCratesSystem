package pl.jordii.mccratessystem.ui.items;

import de.studiocode.invui.gui.impl.PagedGUI;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.controlitem.PageItem;
import org.bukkit.Material;

public class ForwardItem extends PageItem {

    public ForwardItem() {
        super(true);
    }

    @Override
    public ItemProvider getItemProvider(PagedGUI gui) {
        ItemBuilder builder = new ItemBuilder(Material.LEVER);
        builder.setDisplayName("§3Następna Strona")
                .addLoreLines(gui.hasNextPage()
                        ? "§fPrzejdź na stronę §e" + (gui.getCurrentPageIndex() + 2) + "§7/§6" + gui.getPageAmount()
                        : "§cNie ma więcej stron");

        return builder;
    }
}
