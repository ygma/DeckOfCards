package com.example.deckofcards.dao.game;

import com.example.deckofcards.dao.card.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private String id;
    private List<Card> unDealtCards;
}
