package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.dao.game.Player;
import com.example.deckofcards.restfulapi.controller.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
                new Link("/games/" + gameId + "/players/" + player.getId() + "/cards", LinkRels.DEAL_CARD, LinkTypes.POST)
        ));
    }

    @DeleteMapping("/games/{gameId}/players/{playerId}")
    public void addPlayer(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId) {
        Game game = gameRepository.get(gameId);
        game.getPlayerMap().remove(playerId);
        gameRepository.save(game);
    }
}
