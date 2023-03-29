package pl.jordii.mccratessystem;

import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.jordii.mccratessystem.database.MySQLCrateService;
import pl.jordii.mccratessystem.database.MySQLLocationService;
import pl.jordii.mccratessystem.database.MySQLOpeningHistoryService;
import pl.jordii.mccratessystem.database.MySQLUserService;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.database.model.ParticlesAnimationType;
import pl.jordii.mccratessystem.database.mysql.MySQLConnection;
import pl.jordii.mccratessystem.ui.CrateMenu;
import pl.jordii.mccratessystem.util.BlockBoundaries;
import pl.jordii.mccratessystem.util.ColorNmFormatter;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.color.DustData;
import xyz.xenondevs.particle.task.TaskManager;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CratesRepository implements Listener {

    private static MySQLCrateService crateService;
    private static MySQLLocationService locationService;
    private static MySQLUserService userService;
    private static MySQLOpeningHistoryService openingHistoryService;

    private static Map<Location, String> crateLocations = Maps.newHashMap();
    private static Map<String, Crate> cratesList = Maps.newHashMap();

    public CratesRepository(MySQLConnection mySQLConnection) {
        try {
            crateService = new MySQLCrateService(mySQLConnection);
            crateService.createTable();

            locationService = new MySQLLocationService(mySQLConnection);
            locationService.createTable();

            userService = new MySQLUserService(mySQLConnection);
            userService.createTable();

            openingHistoryService = new MySQLOpeningHistoryService(mySQLConnection);
            openingHistoryService.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setupCacheMaps();
        setupParticles();
    }

    @EventHandler
    private void handlePlayerInteractCrateEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getLocation() == null) return;

        if (crateLocations.containsKey(event.getClickedBlock().getLocation())) {
            new CrateMenu(event.getPlayer(), cratesList.get(crateLocations.get(event.getClickedBlock().getLocation())));
            event.setCancelled(true);
        }
    }

    private void setupCacheMaps() {
        CompletableFuture<List<Crate>> crateFuture = crateService.findAll();
        CompletableFuture<Map<Location, String>> locationFuture = locationService.findAll();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(crateFuture, locationFuture);

        allFutures.thenRun(() -> {
            try {
                List<Crate> crates = crateFuture.get();
                cratesList = crates.stream().collect(Collectors.toMap(Crate::getCrateName, Function.identity()));

                Map<Location, String> locations = locationFuture.get();
                locations.forEach((loc, crateNames) -> {
                    crateLocations.put(loc, crateNames);
                });
                setupParticles();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupParticles() {
        crateLocations.forEach((loc, crateName) -> {
            final Crate crate = cratesList.get(crateName);
            startChestBoundParticlesTask(loc, ColorNmFormatter.getColor(crate.getParticlesColor()), crate.getParticlesAnimationType());
        });
    }

    private int startChestBoundParticlesTask(Location location, Color color, ParticlesAnimationType particlesAnimationType) {
        List<Object> packets = new ArrayList<>();
        ParticleBuilder particleBuilder;
        switch (particlesAnimationType) {
            case BOUNDING_BOX:
                particleBuilder = new ParticleBuilder(ParticleEffect.REDSTONE).setParticleData(new DustData(color, 0.5f));
                BlockBoundaries.getBlockBoundaries(location).forEach(loc -> {
                    packets.add(particleBuilder.setLocation(loc).toPacket());
                });
                break;
            default:
                return 0;
        }
        return TaskManager.startWorldTask(packets, 3*1, location.getWorld());
    }

    public static MySQLCrateService getMySQLCrateService() {
        return crateService;
    }

    public static MySQLUserService getMySQLUserService() {
        return userService;
    }

    public static MySQLLocationService getMySQLLocationService() {
        return locationService;
    }

    public static MySQLOpeningHistoryService getMySQLOpeningHistoryService() {
        return openingHistoryService;
    }

    public static Map<Location, String> getCrateLocations() {
        return crateLocations;
    }

    public static Map<String, Crate> getCratesList() {
        return cratesList;
    }

}
