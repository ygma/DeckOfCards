package com.example.deckofcards.dao.deck;

import java.util.List;

public interface DeckRepository {
    void save(Deck deck);

    List<Deck> getAll();

    void reset();

    Deck consume();
}
