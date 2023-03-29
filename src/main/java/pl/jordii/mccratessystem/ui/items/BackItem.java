package pl.jordii.mccratessystem.ui.items;

import de.studiocode.invui.gui.impl.PagedGUI;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.controlitem.PageItem;
import org.bukkit.Material;

public class BackItem extends PageItem {

    public BackItem() {
        super(false);
    }

    @Override
    public ItemProvider getItemProvider(PagedGUI gui) {
        ItemBuilder builder = new ItemBuilder(Material.LEVER);
        builder.setDisplayName("§3Poprzednia Strona")
                .addLoreLines(gui.hasPageBefore()
                        ? "§fPrzejdź do strony §e" + gui.getCurrentPageIndex() + "§7/§6" + gui.getPageAmount()
                        : "§cNie ma poprzedniej strony");

        return builder;
    }

}
