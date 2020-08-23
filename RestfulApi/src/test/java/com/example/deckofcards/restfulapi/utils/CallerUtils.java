package com.example.deckofcards.restfulapi.utils;

import com.example.deckofcards.restfulapi.controller.response.Link;
import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.example.deckofcards.restfulapi.controller.response.ResourceCreatedResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

public class CallerUtils {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    public CallerUtils(MockMvc mockMvc) {

        objectMapper = new ObjectMapper();
        this.mockMvc = mockMvc;
    }

    public <TResponse> TResponse callApi(
            HttpMethod httpMethod,
            String url,
            TypeReference<TResponse> typeReference) throws Exception {

        ResultActions resultActions = callApi(httpMethod, url, status().isOk());
        return deserialize(typeReference, resultActions);
    }

    public ResultActions callApi(HttpMethod httpMethod, String url, ResultMatcher resultMatcher) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = request(httpMethod, url);
        return mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(resultMatcher);
    }

    public void callApi(Link link) throws Exception {
        callApi(HttpMethod.resolve(link.getType()), link.getHref(), status().isOk());
    }

    public <TResponse> TResponse callApi(Link link, TypeReference<TResponse> typeReference) throws Exception {
        ResultActions resultActions = callApi(HttpMethod.resolve(link.getType()), link.getHref(), status().isOk());
        return deserialize(typeReference, resultActions);
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
    public List<Link> getRootLinks() {
        return callApi(
                HttpMethod.GET,
                "/",
                new TypeReference<LinksResponse<String>>() {
                }).getLinks();
    }

    public LinksResponse<ResourceCreatedResponse> callApiToCreateResource(Link linkToCreateGame) throws Exception {
        return callApi(
                linkToCreateGame,
                new TypeReference<LinksResponse<ResourceCreatedResponse>>() {
                });
    }
}
