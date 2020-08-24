package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.deck.Deck;
import com.example.deckofcards.dao.deck.DeckRepository;
import com.example.deckofcards.dao.game.Game;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.restfulapi.controller.response.*;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
public class GameController {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private DeckRepository deckRepository;

    @PostMapping("/games")
    public LinksResponse<ResourceCreatedResponse> createGame() {
        Game game = new Game(
                UUID.randomUUID().toString(),
                new ArrayList<>(),
                new HashMap<>());

        gameRepository.save(game);

        var response = new LinksResponse<>(
                new ResourceCreatedResponse(game.getId()),
                new ArrayList<>());

        response.getLinks().add(new Link("/games/" + game.getId(), LinkRels.DELETE_GAME, LinkTypes.DELETE));
        response.getLinks().add(new Link("/games/" + game.getId() + "/players", LinkRels.ADD_PLAYER_TO_GAME, LinkTypes.POST));
        response.getLinks().add(new Link("/games/" + game.getId() + "/players", LinkRels.LIST_PLAYERS, LinkTypes.GET));
        response.getLinks().add(new Link("/games/" + game.getId() + "/cards/undealt/suits", LinkRels.UNDEALT_CARDS_PER_SUIT, LinkTypes.GET));
        response.getLinks().add(new Link("/games/" + game.getId() + "/cards/undealt/suits/values", LinkRels.UNDEALT_CARD_COUNT_BY_SUIT_AND_VALUE, LinkTypes.GET));

        List<Deck> decks = deckRepository.getAll();
        if (!decks.isEmpty()) {
            Link link = new Link(
                    "/games/" + game.getId() + "/decks",
                    LinkRels.ADD_DECK_TO_GAME,
                    LinkTypes.POST);
            response.getLinks().add(link);
        }

        return response;
    }

    @DeleteMapping("/games/{id}")
    public void deleteGame(@PathVariable("id") String gameId) {
        Game game = gameRepository.get(gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        gameRepository.delete(game);
    }

}
