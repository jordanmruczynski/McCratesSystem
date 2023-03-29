package pl.jordii.mccratessystem.database.services;

import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.database.model.OpeningHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface HistoryService {

    CompletableFuture<List<OpeningHistory>> findAll(UUID uuid);

    CompletableFuture<Void> create(OpeningHistory openingHistory);
}
