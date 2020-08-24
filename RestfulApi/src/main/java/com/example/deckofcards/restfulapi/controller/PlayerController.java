package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.card.Card;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.dao.game.Player;
import com.example.deckofcards.restfulapi.controller.response.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@RestController
public class PlayerController {
    @Autowired
    private GameRepository gameRepository;

    @PostMapping("/games/{gameId}/players")
    public LinksResponse<ResourceCreatedResponse> addPlayer(@PathVariable("gameId") String gameId) {
        Game game = gameRepository.get(gameId);
        Player player = new Player(UUID.randomUUID().toString(), emptyList());
        game.getPlayerMap().put(player.getId(), player);
        gameRepository.save(game);

        return new LinksResponse<>(new ResourceCreatedResponse(player.getId()), asList(
                new Link("/games/" + gameId + "/players/" + player.getId(), LinkRels.REMOVE_PLAYER_FROM_GAME, LinkTypes.DELETE),
                new Link("/games/" + gameId + "/players/" + player.getId() + "/cards", LinkRels.DEAL_CARD, LinkTypes.POST),
                new Link("/games/" + gameId + "/players/" + player.getId() + "/cards", LinkRels.LIST_CARDS, LinkTypes.GET)
        ));
    }

    @DeleteMapping("/games/{gameId}/players/{playerId}")
    public void addPlayer(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId) {
        Game game = gameRepository.get(gameId);
        game.getPlayerMap().remove(playerId);
        gameRepository.save(game);
    }

    @GetMapping("/games/{gameId}/players")
    public LinksResponse<List<PlayerResponse>> listPlayers(@PathVariable("gameId") String gameId) {
        Game game = gameRepository.get(gameId);

        List<PlayerResponse> playerResponses = game.getPlayerMap().values().stream()
                .map(player -> new PlayerResponse(player.getId(), getTotalValue(player.getCards())))
                .sorted(Comparator.comparing(PlayerResponse::getTotalValue).reversed())
                .collect(Collectors.toList());

        return new LinksResponse<>(playerResponses, emptyList());
    }

    private int getTotalValue(List<Card> cards) {
        return cards.stream()
                .map(card -> card.getValue().value)
                .reduce(0, Integer::sum);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerResponse{
        private String playerId;
        private int totalValue;
    }
}
