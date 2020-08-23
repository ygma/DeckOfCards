package com.example.deckofcards.dao.game;

import com.example.deckofcards.dao.BaseRepositoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GameRepositoryImpl  extends BaseRepositoryImpl implements GameRepository {
    private Map<String, Game> gameMap = new HashMap<>();

    @Override
    public Game get(String gameId) {
        return deepCopy(gameMap.getOrDefault(gameId, null), Game.class);
    }

    @Override
    public void save(Game game) {
        gameMap.put(game.getId(), deepCopy(game, Game.class));
    }

    @Override
    public List<Game> getAll() {
        return deepCopy(new ArrayList<>(gameMap.values()), new TypeReference<List<Game>>() {});
    }

    @Override
    public void delete(Game game) {
        gameMap.remove(game.getId());
    }

    @Override
    public void reset() {
        gameMap.clear();
    }
}
