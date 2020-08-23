package com.example.deckofcards.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class BaseRepositoryImpl {
    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    protected <T> T deepCopy(T value, Class<T> clazz) {
        String s = objectMapper.writeValueAsString(value);
        T t = objectMapper.readValue(s, clazz);
        return t;
    }

    @SneakyThrows
    protected <T> T deepCopy(T value, TypeReference<T> typeReference) {
        String s = objectMapper.writeValueAsString(value);
        T t = objectMapper.readValue(s, typeReference);
        return t;
    }
}
