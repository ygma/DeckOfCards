package com.example.deckofcards.restfulapi.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinksResponse<T> {
    private T data;
    private List<Link> links;
}
