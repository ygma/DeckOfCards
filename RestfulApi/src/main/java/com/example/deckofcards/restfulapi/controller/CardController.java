package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.card.Card;
import com.example.deckofcards.dao.card.CardSuit;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.dao.game.Player;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

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

    @GetMapping("/games/{gameId}/players/{playerId}/cards")
    public LinksResponse<List<Card>> getCards(@PathVariable("gameId") String gameId,
            @PathVariable("playerId") String playerId) {

        Game game = gameRepository.get(gameId);
        Player player = game.getPlayerMap().get(playerId);
        return new LinksResponse<>(player.getCards(), emptyList());
    }

    @GetMapping("/games/{gameId}/cards/undealt/suits")
    public LinksResponse<List<SuitResponse>> getUndealtBySuit(@PathVariable("gameId") String gameId) {
        Game game = gameRepository.get(gameId);
        Map<CardSuit, SuitResponse> map = new HashMap<>();
        map.put(CardSuit.HEART, new SuitResponse(CardSuit.HEART, 0));
        map.put(CardSuit.SPADE, new SuitResponse(CardSuit.SPADE, 0));
        map.put(CardSuit.CLUB, new SuitResponse(CardSuit.CLUB, 0));
        map.put(CardSuit.DIAMOND, new SuitResponse(CardSuit.DIAMOND, 0));

        game.getUnDealtCards().forEach(card -> {
            SuitResponse suitResponse = map.get(card.getSuit());
            suitResponse.count++;
        });

        return new LinksResponse<>(new ArrayList<>(map.values()), emptyList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SuitResponse {
        private CardSuit suit;
        private int count;
    }
}
