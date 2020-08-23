package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import com.example.deckofcards.restfulapi.controller.response.LinkTypes;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Arrays.asList;

@RestController
public class RootController {
    @GetMapping("/")
    public LinksResponse<String> get() {
        return new LinksResponse<>(null, asList(
                new Link("/games", LinkRels.CREATE_GAME, LinkTypes.POST),
                new Link("/decks", LinkRels.CREATE_DECK, LinkTypes.POST)
        ));
    }
}
