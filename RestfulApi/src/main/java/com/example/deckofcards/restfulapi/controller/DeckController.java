package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.deck.Deck;
import com.example.deckofcards.dao.deck.DeckRepository;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.restfulapi.controller.response.ResourceCreatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Arrays.asList;

@RestController
public class DeckController {
    @Autowired
    private DeckRepository deckRepository;

    @PostMapping("/decks")
    public LinksResponse<ResourceCreatedResponse> createDeck() {
        Deck deck = new Deck();
        deckRepository.save(deck);

        return new LinksResponse<>(new ResourceCreatedResponse(deck.getId()), asList());
    }
}
