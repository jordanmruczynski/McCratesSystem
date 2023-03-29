package pl.jordii.mccratessystem.util;

import de.studiocode.invui.item.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class LoadoutConverter {

    private LoadoutConverter() { //blokada tworzenia konstruktora
        throw new AssertionError();
    }

    public static String encodeLoadout(List<ItemStack> loadout) {
        try {
            ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOutput = new BukkitObjectOutputStream(byteArrayOutput);

            bukkitOutput.writeObject(loadout);

            return Base64.getEncoder().encodeToString(byteArrayOutput.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String encodeLoadoutSingleItem(ItemStack loadout) {
        try {
            ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOutput = new BukkitObjectOutputStream(byteArrayOutput);

            bukkitOutput.writeObject(loadout);

            return Base64.getEncoder().encodeToString(byteArrayOutput.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static List<ItemStack> decodeLoadout(String base64) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64);
            ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(decoded);
            BukkitObjectInputStream bukkitInput = new BukkitObjectInputStream(byteArrayInput);

            List<ItemStack> loadout = (List<ItemStack>) bukkitInput.readObject();

            return loadout.stream().filter(itemStack -> itemStack != null).collect(Collectors.toList());
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static ItemStack decodeLoadoutSingleItem(String base64) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64);
            ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(decoded);
            BukkitObjectInputStream bukkitInput = new BukkitObjectInputStream(byteArrayInput);

            ItemStack loadout = (ItemStack) bukkitInput.readObject();
            return loadout;
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
