package com.example.deckofcards.dao.game;

import com.example.deckofcards.dao.card.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private String id;
    private List<Card> cards;
}
