package com.example.deckofcards.dao.game;

import java.util.List;

public interface GameRepository {
    Game get(String gameId);

    void save(Game game);

    List<Game> getAll();

    void delete(Game game);
}
