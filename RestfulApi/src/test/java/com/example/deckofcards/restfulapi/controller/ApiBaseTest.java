package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.deck.DeckRepository;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.restfulapi.controller.response.ResourceCreatedResponse;
import com.example.deckofcards.restfulapi.utils.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiBaseTest {

    protected CallerUtils callerUtils;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected GameRepository gameRepository;
    @Autowired
    protected DeckRepository deckRepository;

    @BeforeEach
    protected void beforeEachBase() {
        callerUtils = new CallerUtils(mockMvc);
        gameRepository.reset();
        deckRepository.reset();
    }

    protected <TResponse> TResponse callApi(
            HttpMethod httpMethod,
            String url,
            TypeReference<TResponse> typeReference) throws Exception {

        return callerUtils.callApi(httpMethod, url, typeReference);
    }

    protected ResultActions callApi(HttpMethod httpMethod, String url, ResultMatcher resultMatcher) throws Exception {
        return callerUtils.callApi(httpMethod, url, resultMatcher);
    }

    protected void callApi(Link link) throws Exception {
        callerUtils.callApi(link);
    }

    protected  <TResponse> TResponse callApi(Link link, TypeReference<TResponse> typeReference) throws Exception {
        return callerUtils.callApi(link, typeReference);
    }

    @SneakyThrows
    protected List<Link> getRootLinks() {
        return callerUtils.getRootLinks();
    }

    protected LinksResponse<ResourceCreatedResponse> callApiToCreateResource(Link linkToCreateGame) throws Exception {
        return callerUtils.callApiToCreateResource(linkToCreateGame);
    }

    protected SimpleTestContext buildSimpleTestContext() {
        return SimpleTestContext.build(callerUtils);
    }
}
