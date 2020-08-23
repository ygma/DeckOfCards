package com.example.deckofcards.dao.deck;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DeckRepositoryImpl implements DeckRepository {
    private Map<String, Deck> deckMap = new HashMap<>();

    @Override
    public void save(Deck deck) {
        deckMap.put(deck.getId(), deck);
    }

    @Override
    public Deck get(String id) {
        return deckMap.getOrDefault(id, null);
    }
}
