package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.dao.deck.DeckRepository;
import com.example.deckofcards.dao.game.GameRepository;
import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.restfulapi.controller.response.ResourceCreatedResponse;
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

    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected GameRepository gameRepository;
    @Autowired
    protected DeckRepository deckRepository;

    @BeforeEach
    protected void beforeEachBase() {
        objectMapper = new ObjectMapper();
        gameRepository.reset();
        deckRepository.reset();
    }

    protected <TResponse> TResponse callApi(
            HttpMethod httpMethod,
            String url,
            TypeReference<TResponse> typeReference) throws Exception {

        ResultActions resultActions = callApi(httpMethod, url, status().isOk());
        return deserialize(typeReference, resultActions);
    }

    protected ResultActions callApi(HttpMethod httpMethod, String url, ResultMatcher resultMatcher) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = request(httpMethod, url);
        return mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(resultMatcher);
    }

    protected void callApi(Link link) throws Exception {
        callApi(HttpMethod.resolve(link.getType()), link.getHref(), status().isOk());
    }

    protected <TResponse> TResponse callApi(Link link, TypeReference<TResponse> typeReference) throws Exception {
        ResultActions resultActions = callApi(HttpMethod.resolve(link.getType()), link.getHref(), status().isOk());
        return deserialize(typeReference, resultActions);
    }

    protected Link getLink(List<Link> links, String rel) {
        return links.stream()
                .filter(link -> link.getRel().equals(rel))
                .findFirst()
                .orElse(null);
    }

    private <TResponse> TResponse deserialize(
            TypeReference<TResponse> typeReference,
            ResultActions resultActions) throws UnsupportedEncodingException, com.fasterxml.jackson.core.JsonProcessingException {
        String contentAsString = resultActions
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(contentAsString, typeReference);
    }

    @SneakyThrows
    protected List<Link> getRootLinks() {
        return callApi(
                HttpMethod.GET,
                "/",
                new TypeReference<LinksResponse<String>>() {
                }).getLinks();
    }

    protected LinksResponse<ResourceCreatedResponse> callApiToCreateResource(Link linkToCreateGame) throws Exception {
        return callApi(
                linkToCreateGame,
                new TypeReference<LinksResponse<ResourceCreatedResponse>>() {
                });
    }
}
