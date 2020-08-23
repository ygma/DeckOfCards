package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.card.Card;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.dao.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {
    @Autowired
    private GameRepository gameRepository;

    @PostMapping("/games/{gameId}/players/{playerId}/cards")
    public void dealCard(@PathVariable("gameId") String gameId,
            @PathVariable("playerId") String playerId) {

        Game game = gameRepository.get(gameId);
        Player player = game.getPlayerMap().get(playerId);
        List<Card> unDealtCards = game.getUnDealtCards();

        if (unDealtCards.size() == 0) {
            return;
        }
        Card card = unDealtCards.remove(0);
        player.getCards().add(card);
        gameRepository.save(game);
    }
}
