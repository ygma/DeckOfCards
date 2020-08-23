package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.GameController.GameResponse;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest extends ApiBaseTest {
    @Autowired
    private GameRepository gameRepository;

    @Test
    @SneakyThrows
    public void should_create_game() {
        LinksResponse<GameResponse> actual = callApi(
                HttpMethod.POST,
                "/games", new TypeReference<LinksResponse<GameResponse>>() {});

        List<Game> actualGames = gameRepository.getAll();

        assertTrue(actualGames.size() > 0);
        assertEquals(actual.getData().getId(), actualGames.get(0).getId());
    }
}
