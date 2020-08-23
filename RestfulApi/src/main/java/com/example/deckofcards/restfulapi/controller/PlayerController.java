package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.dao.game.Player;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.restfulapi.controller.response.ResourceCreatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static java.util.Collections.emptyList;

@RestController
public class PlayerController {
    @Autowired
    private GameRepository gameRepository;

    @PostMapping("/games/{gameId}/players")
    public LinksResponse<ResourceCreatedResponse> addPlayer(@PathVariable("gameId") String gameId) {
        Game game = gameRepository.get(gameId);
        Player player = new Player(UUID.randomUUID().toString());
        game.getPlayerMap().put(player.getId(), player);
        gameRepository.save(game);

        return new LinksResponse<>(new ResourceCreatedResponse(player.getId()), emptyList());
    }

    @DeleteMapping("/games/{gameId}/players/{playerId}")
    public void addPlayer(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId) {
        Game game = gameRepository.get(gameId);
        game.getPlayerMap().remove(playerId);
        gameRepository.save(game);
    }
}
