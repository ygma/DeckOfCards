package com.example.deckofcards.dao.deck;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DeckRepositoryImpl implements DeckRepository {
    private LinkedList<Deck> decks = new LinkedList<>();

    @Override
    public void save(Deck deck) {
        decks.offer(deck);
    }

    @Override
    public List<Deck> getAll() {
        return decks;
    }

    @Override
    public void reset() {
        decks.clear();
    }

    @Override
    public Deck consume() {
        if (decks.isEmpty()) {
            return null;
        }
        return decks.poll();
    }
}
