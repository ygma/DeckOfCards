package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.Player;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest extends ApiBaseTest {
    @Test
    @SneakyThrows
    public void should_deal_a_card_to_a_player() {
        Link linkToCreateDeck = getLink(getRootLinks(), LinkRels.CREATE_DECK);
        callApi(linkToCreateDeck);

        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddDeck = getLink(gameResponse.getLinks(), LinkRels.ADD_DECK_TO_GAME);
        callApi(linkToAddDeck);

        Link linkToAddPlayer = getLink(gameResponse.getLinks(), LinkRels.ADD_PLAYER_TO_GAME);
        var playerResponse = callApiToCreateResource(linkToAddPlayer);
        String playerId = playerResponse.getData().getId();

        Link linkToDealCard = getLink(playerResponse.getLinks(), LinkRels.DEAL_CARD);
        assertNotNull(linkToDealCard);

        String gameId = gameResponse.getData().getId();
        Game game = gameRepository.get(gameId);
        assertEquals(52, game.getUnDealtCards().size());
        assertEquals(0, game.getPlayerMap().get(playerId).getCards().size());

        callApi(linkToDealCard);

        game = gameRepository.get(gameId);
        assertEquals(51, game.getUnDealtCards().size());
        assertEquals(1, game.getPlayerMap().get(playerId).getCards().size());
    }

    @Test
    @SneakyThrows
    public void should_return_200_and_not_deal_a_card_to_a_player_if_no_cards_is_undealt() {
        Link linkToCreateDeck = getLink(getRootLinks(), LinkRels.CREATE_DECK);
        callApi(linkToCreateDeck);

        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddDeck = getLink(gameResponse.getLinks(), LinkRels.ADD_DECK_TO_GAME);
        callApi(linkToAddDeck);

        Link linkToAddPlayer = getLink(gameResponse.getLinks(), LinkRels.ADD_PLAYER_TO_GAME);
        var playerResponse = callApiToCreateResource(linkToAddPlayer);
        String playerId = playerResponse.getData().getId();

        Link linkToDealCard = getLink(playerResponse.getLinks(), LinkRels.DEAL_CARD);
        assertNotNull(linkToDealCard);

        String gameId = gameResponse.getData().getId();
        Game game = gameRepository.get(gameId);
        assertEquals(52, game.getUnDealtCards().size());
        assertEquals(0, game.getPlayerMap().get(playerId).getCards().size());

        for (int i = 0; i < 53; i++) {
            callApi(linkToDealCard);
        }

        game = gameRepository.get(gameId);
        assertEquals(0, game.getUnDealtCards().size());
        assertEquals(52, game.getPlayerMap().get(playerId).getCards().size());
    }
}
