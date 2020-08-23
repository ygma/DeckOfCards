package com.example.deckofcards.dao.deck;

import com.example.deckofcards.dao.BaseRepositoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DeckRepositoryImpl extends BaseRepositoryImpl implements DeckRepository {
    private LinkedList<Deck> decks = new LinkedList<>();

    @Override
    public void save(Deck deck) {
        decks.offer(deepCopy(deck, Deck.class));
    }

    @Override
    public List<Deck> getAll() {
        return deepCopy(decks, new TypeReference<List<Deck>>() {});
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
