package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest extends ApiBaseTest {
    @Test
    @SneakyThrows
    public void should_add_player_to_a_game() {
        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddPlayer = getLink(gameResponse.getLinks(), LinkRels.ADD_PLAYER_TO_GAME);
        assertNotNull(linkToAddPlayer);

        var actualPlayCreatedResponse = callApiToCreateResource(linkToAddPlayer);
        Game game = gameRepository.get(gameResponse.getData().getId());
        assertEquals(1, game.getPlayerMap().size());
        assertTrue(game.getPlayerMap().containsKey(actualPlayCreatedResponse.getData().getId()));
    }
}
