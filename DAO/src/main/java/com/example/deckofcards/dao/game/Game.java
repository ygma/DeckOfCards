package com.example.deckofcards.dao.game;

import com.example.deckofcards.dao.card.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private String id;
    private List<Card> unDealtCards;
    private Map<String, Player> playerMap;
}
