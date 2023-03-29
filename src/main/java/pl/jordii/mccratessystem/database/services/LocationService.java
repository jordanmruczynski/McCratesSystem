package pl.jordii.mccratessystem.database.services;

import org.bukkit.Location;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface LocationService {

    CompletableFuture<String> find(Location location);

    //create findAll method
  //  Map<Location, String> findAll();
    CompletableFuture<Map<Location, Map<Integer, String>>> findAllWithIds();

    CompletableFuture<Map<Location, String>> findAll();

    CompletableFuture<Void> save(String crateName, Location location);

    CompletableFuture<Void> delete(String crateName, Location location);

    CompletableFuture<Void> delete(int id);

    CompletableFuture<Void> deleteAll(String crateName);
}
