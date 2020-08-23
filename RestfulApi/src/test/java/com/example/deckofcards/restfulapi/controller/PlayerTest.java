package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.Test;

import static com.example.deckofcards.restfulapi.utils.LinkUtils.getLink;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest extends ApiBaseTest {
    @Test
    @SneakyThrows
    public void should_add_player_to_a_game() {
        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddPlayer = getLink(gameResponse.getLinks(), LinkRels.ADD_PLAYER_TO_GAME);
        assertNotNull(linkToAddPlayer);

        var actualPlayerCreatedResponse = callApiToCreateResource(linkToAddPlayer);
        Game game = gameRepository.get(gameResponse.getData().getId());
        assertEquals(1, game.getPlayerMap().size());
        assertTrue(game.getPlayerMap().containsKey(actualPlayerCreatedResponse.getData().getId()));
    }

    @Test
    @SneakyThrows
    public void should_delete_player_from_a_game() {
        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddPlayer = getLink(gameResponse.getLinks(), LinkRels.ADD_PLAYER_TO_GAME);
        var playerResponse = callApiToCreateResource(linkToAddPlayer);

        Link linkToRemovePlayer = getLink(playerResponse.getLinks(), LinkRels.REMOVE_PLAYER_FROM_GAME);
        callApi(linkToRemovePlayer);

        String gameId = gameResponse.getData().getId();
        Game game = gameRepository.get(gameId);
        assertEquals(0, game.getPlayerMap().size());
    }
}
