package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.card.Card;
import com.example.deckofcards.dao.card.CardSuit;
import com.example.deckofcards.dao.card.CardValue;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.dao.game.Player;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
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

    @GetMapping("/games/{gameId}/cards/undealt/suits/values")
    public LinksResponse<List<SuitValueResponse>> getUndealtCountBySuitValue(@PathVariable("gameId") String gameId) {
        Game game = gameRepository.get(gameId);

        Map<Card, SuitValueResponse> map = new HashMap<>();

        game.getUnDealtCards().forEach(card -> {
            if (!map.containsKey(card)) {
                map.put(card, new SuitValueResponse(card.getSuit(), card.getValue(), 0));
            }
            SuitValueResponse suitValueResponse = map.get(card);
            suitValueResponse.count++;
        });

        List<SuitValueResponse> list = map.values().stream()
                .sorted(Comparator.comparing(SuitValueResponse::getSuit).thenComparing((response1, response2) -> response2.getValue().compareTo(response1.getValue())))
                .collect(Collectors.toList());

        return new LinksResponse<>(list, emptyList());
    }

    @PostMapping("/games/{gameId}/cards/undealt/shuffle")
    public void shuffle(@PathVariable("gameId") String gameId) {
        Game game = gameRepository.get(gameId);

        Card[] array = game.getUnDealtCards().toArray(new Card[0]);
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            int nextInt = random.nextInt(array.length);
            Card temp = array[i];
            array[i] = array[nextInt];
            array[nextInt] = temp;
        }
        game.setUnDealtCards(asList(array));
        gameRepository.save(game);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SuitResponse {
        private CardSuit suit;
        private int count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SuitValueResponse {
        private CardSuit suit;
        private CardValue value;
        private int count;
    }
}
