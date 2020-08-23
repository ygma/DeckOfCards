package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static java.util.Arrays.asList;

@RestController
@Lazy
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @PostMapping("/games")
    public LinksResponse<GameResponse> createGame() {
        Game game = new Game(UUID.randomUUID().toString());
        gameRepository.save(game);
        return new LinksResponse<>(new GameResponse(game.getId()), asList(
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GameResponse {
        private String id;
    }
}
