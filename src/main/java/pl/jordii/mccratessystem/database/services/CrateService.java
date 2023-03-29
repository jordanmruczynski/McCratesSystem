package pl.jordii.mccratessystem.database.services;

import pl.jordii.mccratessystem.database.model.Crate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CrateService {

    CompletableFuture<List<Crate>> findAll();

    CompletableFuture<Crate> find(String crateName);

    CompletableFuture<Void> create(Crate crate);

    CompletableFuture<Void> updateStatus(String crateName, boolean status);

    CompletableFuture<Void> delete(String crateName);

    CompletableFuture<Void> update(Crate crate);
}
