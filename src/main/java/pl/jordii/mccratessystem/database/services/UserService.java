package pl.jordii.mccratessystem.database.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<Integer> keys(UUID uuid, String crateName);

    CompletableFuture<Void> update(UUID uuid, String playerName, String crateName, int keys);

    CompletableFuture<Void> save(UUID uuid, String playerName, String crateName, int keys);

    CompletableFuture<Void> delete(UUID uuid, String crateName);

}
