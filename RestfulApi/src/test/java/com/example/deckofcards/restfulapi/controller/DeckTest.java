package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.deck.Deck;
import com.example.deckofcards.dao.deck.DeckRepository;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.restfulapi.controller.response.ResourceCreatedResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeckTest extends ApiBaseTest{
    @Autowired
    private DeckRepository deckRepository;

    @Test
    @SneakyThrows
    public void should_create_a_deck() {
        Link link = getLink(getRootLinks(), LinkRels.CREATE_DECK);

        LinksResponse<ResourceCreatedResponse> actual = callApi(
                link,
                new TypeReference<LinksResponse<ResourceCreatedResponse>>() {
                });

        Deck actualDeck = deckRepository.get(actual.getData().getId());
        assertNotNull(actualDeck);
    }

}
