package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import com.example.deckofcards.restfulapi.controller.response.LinkTypes;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RootTest extends ApiBaseTest {

    @Test
    @SneakyThrows
    public void should_return_games_and_decks_link() {
        LinksResponse<String> actual = callApi(
                HttpMethod.GET,
                "/",
                new TypeReference<LinksResponse<String>>() {});

        LinksResponse<String> expected = new LinksResponse<>(null, asList(
                new Link("/games", LinkRels.CREATE_GAME, LinkTypes.POST),
                new Link("/decks", LinkRels.CREATE_DECK, LinkTypes.POST)
        ));
        assertEquals(expected, actual);
    }
}
