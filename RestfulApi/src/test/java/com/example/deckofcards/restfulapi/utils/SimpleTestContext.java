package com.example.deckofcards.restfulapi.utils;

import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinkRels;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.restfulapi.controller.response.ResourceCreatedResponse;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;

import static com.example.deckofcards.restfulapi.utils.LinkUtils.getLink;

@Getter
public class SimpleTestContext {
    private CallerUtils callerUtils;
    private LinksResponse<ResourceCreatedResponse> gameResponse;
    private LinksResponse<ResourceCreatedResponse> playerResponse;

    private SimpleTestContext(CallerUtils callerUtils) throws Exception {
        this.callerUtils = callerUtils;

        Link linkToCreateDeck = getLink(callerUtils.getRootLinks(), LinkRels.CREATE_DECK);
        callerUtils.callApi(linkToCreateDeck);

        Link linkToCreateGame = getLink(callerUtils.getRootLinks(), LinkRels.CREATE_GAME);
        gameResponse = callerUtils.callApiToCreateResource(linkToCreateGame);

        Link linkToAddDeck = getLink(gameResponse.getLinks(), LinkRels.ADD_DECK_TO_GAME);
        callerUtils.callApi(linkToAddDeck);

        Link linkToAddPlayer = getLink(gameResponse.getLinks(), LinkRels.ADD_PLAYER_TO_GAME);
        playerResponse = callerUtils.callApiToCreateResource(linkToAddPlayer);
    }

    @SneakyThrows
    public static SimpleTestContext build(CallerUtils callerUtils) {
        return new SimpleTestContext(callerUtils);
    }

    public String getGameId() {
        return getGameResponse().getData().getId();
    }

    public String getPlayerId() {
        return getPlayerResponse().getData().getId();
    }

    public Link getLinkFromPlayer(String rel) {
        return getLink(playerResponse.getLinks(), rel);
    }

    public Link getLinkFromGame(String rel) {
        return getLink(gameResponse.getLinks(), rel);
    }
}
