package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.restfulapi.controller.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static java.util.Arrays.asList;

@RestController
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @PostMapping("/games")
    public LinksResponse<ResourceCreatedResponse> createGame() {
        Game game = new Game(UUID.randomUUID().toString());
        gameRepository.save(game);
        return new LinksResponse<>(new ResourceCreatedResponse(game.getId()), asList(
                new Link("/games/" + game.getId(), LinkRels.DELETE_GAME, LinkTypes.DELETE)
        ));
    }

    @DeleteMapping("/games/{id}")
    public void deleteGame(@PathVariable("id") String gameId) {
        Game game = gameRepository.get(gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        gameRepository.delete(game);
    }

}
