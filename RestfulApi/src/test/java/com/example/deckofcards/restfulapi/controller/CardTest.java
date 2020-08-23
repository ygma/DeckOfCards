package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.card.Card;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.restfulapi.utils.SimpleTestContext;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.deckofcards.restfulapi.utils.LinkUtils.getLink;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest extends ApiBaseTest {
    @Test
    @SneakyThrows
    public void should_deal_a_card_to_a_player() {
        SimpleTestContext testContext = buildSimpleTestContext();

        Link linkToDealCard = testContext.getLinkFromPlayer(LinkRels.DEAL_CARD);
        assertNotNull(linkToDealCard);

        String gameId = testContext.getGameId();
        String playerId = testContext.getPlayerId();

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
        SimpleTestContext testContext = buildSimpleTestContext();

        Link linkToDealCard = testContext.getLinkFromPlayer(LinkRels.DEAL_CARD);
        assertNotNull(linkToDealCard);

        String gameId = testContext.getGameId();
        String playerId = testContext.getPlayerId();

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

    @Test
    @SneakyThrows
    public void should_get_the_list_of_cards_for_a_player() {
        SimpleTestContext testContext = buildSimpleTestContext();

        Link linkToDealCard = testContext.getLinkFromPlayer(LinkRels.DEAL_CARD);
        callApi(linkToDealCard);
        callApi(linkToDealCard);

        Link linkToListCards = testContext.getLinkFromPlayer(LinkRels.LIST_CARDS);
        LinksResponse<List<Card>> response = callApi(linkToListCards, new TypeReference<LinksResponse<List<Card>>>() {});
        assertEquals(2, response.getData().size());
    }
}
