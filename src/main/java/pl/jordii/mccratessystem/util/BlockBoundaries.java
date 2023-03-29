package pl.jordii.mccratessystem.util;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class BlockBoundaries {

    private BlockBoundaries() {
        new AssertionError();
    }

    public static List<Location> getBlockBoundaries(Location location) {
        List<Location> result = new ArrayList<>();
        World world = location.getWorld();
        double minX = location.getBlockX();
        double minY = location.getBlockY();
        double minZ = location.getBlockZ();
        double maxX = location.getBlockX() + 1;
        double maxY = location.getBlockY() + 1;
        double maxZ = location.getBlockZ() + 1;

        for (double x = minX; x <= maxX; x = Math.round((x + 0.05) * 1e2) / 1e2) {
            for (double y = minY; y <= maxY; y = Math.round((y + 0.05) * 1e2) / 1e2) {
                for (double z = minZ; z <= maxZ; z = Math.round((z + 0.05) * 1e2) / 1e2) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;

                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }
        return result;
    }
}
