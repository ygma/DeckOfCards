package com.example.deckofcards.dao.deck;

public interface DeckRepository {
    void save(Deck deck);

    Deck get(String id);
}
