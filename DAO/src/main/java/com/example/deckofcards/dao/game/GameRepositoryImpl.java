package com.example.deckofcards.dao.game;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Lazy
public class GameRepositoryImpl implements GameRepository {
    private Map<String, Game> games = new HashMap<>();

    @Override
    public void save(Game game) {
        games.put(game.getId(), game);
    }

    @Override
    public List<Game> getAll() {
        return new ArrayList<>(games.values());
    }
}
