package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

@RestController
@Lazy
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @PostMapping("/games")
    public LinksResponse<GameResponse> createGame() {
        Game game = new Game(UUID.randomUUID().toString());
        gameRepository.save(game);
        return new LinksResponse<>(new GameResponse(game.getId()), Collections.emptyList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GameResponse {
        private String id;
    }
}
