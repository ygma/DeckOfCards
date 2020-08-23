package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.GameController.GameResponse;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameTest extends ApiBaseTest {
    @Autowired
    private GameRepository gameRepository;

    @Test
    @SneakyThrows
    public void should_create_and_delete_game() {
        LinksResponse<GameResponse> actual = callApiToCreateGame();

        assertCreatedGame(actual);

        callApiToDeleteGame(actual);

        assertEquals(0, gameRepository.getAll().size());
    }

    @Test
    @SneakyThrows
    public void should_return_404_if_try_to_delete_a_non_existent_game() {
        callApi(HttpMethod.DELETE, "/games/1234", status().isNotFound());
    }

    private void callApiToDeleteGame(LinksResponse<GameResponse> actual) throws Exception {
        Link deleteLink = actual.getLinks().get(0);
        callApi(deleteLink);
    }

    private LinksResponse<GameResponse> callApiToCreateGame() throws Exception {
        return callApi(
                    HttpMethod.POST,
                    "/games",
                    new TypeReference<LinksResponse<GameResponse>>() {});
    }

    private void assertCreatedGame(LinksResponse<GameResponse> actual) {
        List<Game> actualGames = gameRepository.getAll();
        assertTrue(actualGames.size() > 0);

        Game actualGame = actualGames.get(0);
        LinksResponse<GameResponse> expected = new LinksResponse<>(
                new GameResponse(actualGames.get(0).getId()),
                asList(new Link("/games/" + actualGame.getId(), LinkRels.DELETE_GAME, LinkTypes.DELETE))
        );
        assertEquals(expected, actual);
    }
}
