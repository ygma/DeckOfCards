package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.restfulapi.controller.response.*;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.List;

import static com.example.deckofcards.restfulapi.utils.LinkUtils.getLink;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameTest extends ApiBaseTest {

    @Test
    @SneakyThrows
    public void should_create_and_delete_game() {
        var actual = callApiToCreateGame();

        assertCreatedGame(actual);

        callApiToDeleteGame(actual);

        assertEquals(0, gameRepository.getAll().size());
    }

    @Test
    @SneakyThrows
    public void should_return_404_if_try_to_delete_a_non_existent_game() {
        callApi(HttpMethod.DELETE, "/games/1234", status().isNotFound());
    }

    private void callApiToDeleteGame(LinksResponse<ResourceCreatedResponse> actual) throws Exception {
        Link deleteLink = getLink(actual.getLinks(), LinkRels.DELETE_GAME);
        callApi(deleteLink);
    }

    private LinksResponse<ResourceCreatedResponse> callApiToCreateGame() throws Exception {
        Link link = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        return callApiToCreateResource(link);
    }

    private void assertCreatedGame(LinksResponse<ResourceCreatedResponse> actual) {
        List<Game> actualGames = gameRepository.getAll();
        assertTrue(actualGames.size() > 0);

        Game actualGame = actualGames.get(0);
        var expected = new LinksResponse<>(
                new ResourceCreatedResponse(actualGames.get(0).getId()),
                asList(new Link("/games/" + actualGame.getId(), LinkRels.DELETE_GAME, LinkTypes.DELETE),
                        new Link("/games/" + actualGame.getId() + "/players", LinkRels.ADD_PLAYER_TO_GAME, LinkTypes.POST),
                        new Link("/games/" + actualGame.getId() + "/players", LinkRels.LIST_PLAYERS, LinkTypes.GET),
                        new Link("/games/" + actualGame.getId() + "/cards/undealt/suits", LinkRels.UNDEALT_CARDS_PER_SUIT, LinkTypes.GET),
                        new Link("/games/" + actualGame.getId() + "/cards/undealt/suits/values", LinkRels.UNDEALT_CARD_COUNT_BY_SUIT_AND_VALUE, LinkTypes.GET))
        );
        assertEquals(expected, actual);
    }
}
