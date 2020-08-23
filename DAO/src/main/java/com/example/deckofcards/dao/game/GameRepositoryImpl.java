package com.example.deckofcards.dao.game;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GameRepositoryImpl implements GameRepository {
    private Map<String, Game> gameMap = new HashMap<>();

    @Override
    public Game get(String gameId) {
        return gameMap.getOrDefault(gameId, null);
    }

    @Override
    public void save(Game game) {
        gameMap.put(game.getId(), game);
    }

    @Override
    public List<Game> getAll() {
        return new ArrayList<>(gameMap.values());
    }

    @Override
    public void delete(Game game) {
        gameMap.remove(game.getId());
    }
}
