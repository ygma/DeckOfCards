package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.card.Card;
import com.example.deckofcards.dao.card.CardSuit;
import com.example.deckofcards.dao.card.CardValue;
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
import java.util.Collections;
import java.util.List;

import static com.example.deckofcards.dao.card.CardSuit.*;
import static com.example.deckofcards.restfulapi.utils.LinkUtils.getLink;
import static java.util.Arrays.asList;
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

    @Test
    @SneakyThrows
    public void should_get_the_number_of_undealt_cards_per_suit() {
        SimpleTestContext testContext = buildSimpleTestContext();

        Link link = testContext.getLinkFromGame(LinkRels.UNDEALT_CARDS_PER_SUIT);
        var actualResponse = callApi(link, new TypeReference<LinksResponse<List<CardController.SuitResponse>>>() {});

        List<CardController.SuitResponse> suitResponseList = actualResponse.getData();
        assertEquals(4, suitResponseList.size());

        suitResponseList.forEach(suitResponse -> assertEquals(13, suitResponse.getCount()));
    }

    @Test
    @SneakyThrows
    public void should_get_the_number_of_undealt_cards_sorted_by_suit_and_value() {
        Link linkToCreateDeck = getLink(getRootLinks(), LinkRels.CREATE_DECK);
        callApiToCreateResource(linkToCreateDeck);
        callApiToCreateResource(linkToCreateDeck);

        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddDeckToGame = getLink(gameResponse.getLinks(), LinkRels.ADD_DECK_TO_GAME);
        callApi(linkToAddDeckToGame);
        callApi(linkToAddDeckToGame);

        Link link = getLink(gameResponse.getLinks(), LinkRels.UNDEALT_CARD_COUNT_BY_SUIT_AND_VALUE);
        var actualResponse = callApi(link, new TypeReference<LinksResponse<List<CardController.SuitValueResponse>>>() {});

        List<CardController.SuitValueResponse> suitResponseList = actualResponse.getData();
        List<CardSuit> suits = asList(HEART, SPADE, CLUB, DIAMOND);
        List<CardValue> values = asList(CardValue.values());
        Collections.reverse(values);

        int i = 0;
        for (CardSuit suit : suits) {
            for (CardValue value : values) {
                CardController.SuitValueResponse suitValueResponse = suitResponseList.get(i);
                assertEquals(suit, suitValueResponse.getSuit());
                assertEquals(value, suitValueResponse.getValue());
                assertEquals(2, suitValueResponse.getCount());
                i++;
            }
        }
    }

    @Test
    @SneakyThrows
    public void should_shuffle_undealt_cards() {
        Link linkToCreateDeck = getLink(getRootLinks(), LinkRels.CREATE_DECK);
        callApiToCreateResource(linkToCreateDeck);

        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddDeckToGame = getLink(gameResponse.getLinks(), LinkRels.ADD_DECK_TO_GAME);
        callApi(linkToAddDeckToGame);

        List<Card> originalCardOrder = gameRepository.get(gameResponse.getData().getId()).getUnDealtCards();

        Link link = getLink(gameResponse.getLinks(), LinkRels.SHUFFLE);
        callApi(link);

        List<Card> newOrder = gameRepository.get(gameResponse.getData().getId()).getUnDealtCards();
        assertNotEquals(originalCardOrder, newOrder);
    }
}
