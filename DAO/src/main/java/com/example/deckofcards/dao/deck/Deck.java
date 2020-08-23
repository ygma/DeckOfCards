package com.example.deckofcards.dao.deck;

import com.example.deckofcards.dao.card.Card;
import com.example.deckofcards.dao.card.CardSuit;
import com.example.deckofcards.dao.card.CardValue;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Deck {
    private String id;
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        id = UUID.randomUUID().toString();

        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                cards.add(new Card(suit, value));
            }
        }
    }
}
