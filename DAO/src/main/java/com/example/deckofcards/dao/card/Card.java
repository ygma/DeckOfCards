package com.example.deckofcards.dao.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private CardSuit suit;
    private CardValue value;
}
