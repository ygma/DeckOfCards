package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.deck.Deck;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.deckofcards.restfulapi.utils.LinkUtils.getLink;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest extends ApiBaseTest{

    @Test
    @SneakyThrows
    public void should_create_a_deck() {
        Link link = getLink(getRootLinks(), LinkRels.CREATE_DECK);

        callApiToCreateResource(link);

        List<Deck> actual = deckRepository.getAll();
        assertEquals(1, actual.size());
    }

    @Test
    @SneakyThrows
    public void should_add_a_deck_to_a_game_deck_if_deck_is_available() {
        Link linkToCreateDeck = getLink(getRootLinks(), LinkRels.CREATE_DECK);
        callApiToCreateResource(linkToCreateDeck);

        Link linkToCreateGame = getLink(getRootLinks(), LinkRels.CREATE_GAME);
        var gameResponse = callApiToCreateResource(linkToCreateGame);

        Link linkToAddDeckToGame = getLink(gameResponse.getLinks(), LinkRels.ADD_DECK_TO_GAME);
        assertNotNull(linkToAddDeckToGame);

        String gameId = gameResponse.getData().getId();
        Game game = gameRepository.get(gameId);
        assertEquals(0, game.getUnDealtCards().size());

        callApi(linkToAddDeckToGame);
        game = gameRepository.get(gameId);
        assertEquals(52, game.getUnDealtCards().size());

        gameResponse = callApiToCreateResource(linkToCreateGame);
        assertNull(getLink(gameResponse.getLinks(), LinkRels.ADD_DECK_TO_GAME));
    }
}
