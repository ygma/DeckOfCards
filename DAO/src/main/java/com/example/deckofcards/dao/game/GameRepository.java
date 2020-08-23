package com.example.deckofcards.dao.game;

import java.util.List;

public interface GameRepository {
    void save(Game game);

    List<Game> getAll();
}
