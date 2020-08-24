package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.restfulapi.controller.PlayerController.PlayerResponse;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    @SneakyThrows
    public void should_return_players_and_his_total_values() {
        Link linkToCreateDeck = getLink(callerUtils.getRootLinks(), LinkRels.CREATE_DECK);
        callerUtils.callApi(linkToCreateDeck);

        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddDeck = getLink(gameResponse.getLinks(), LinkRels.ADD_DECK_TO_GAME);
        callerUtils.callApi(linkToAddDeck);

        Link linkToAddPlayer = getLink(gameResponse.getLinks(), LinkRels.ADD_PLAYER_TO_GAME);
        var player1Response = callApiToCreateResource(linkToAddPlayer);

        Link linkToDealCardToPlayer1 = getLink(player1Response.getLinks(), LinkRels.DEAL_CARD);
        callApi(linkToDealCardToPlayer1);
        callApi(linkToDealCardToPlayer1);

        var player2Response = callApiToCreateResource(linkToAddPlayer);

        Link linkToDealCardToPlayer2 = getLink(player2Response.getLinks(), LinkRels.DEAL_CARD);
        callApi(linkToDealCardToPlayer2);
        callApi(linkToDealCardToPlayer2);

        Link linkToListPlayers = getLink(gameResponse.getLinks(), LinkRels.LIST_PLAYERS);
        LinksResponse<List<PlayerResponse>> response = callApi(
                linkToListPlayers,
                new TypeReference<LinksResponse<List<PlayerResponse>>>() {});

        List<PlayerResponse> actualPlayers = response.getData();
        assertEquals(2, actualPlayers.size());
        assertTrue(actualPlayers.get(0).getTotalValue() > actualPlayers.get(1).getTotalValue());
    }
}
