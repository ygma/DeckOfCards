package com.example.deckofcards.restfulapi.utils;

import com.example.deckofcards.restfulapi.controller.response.Link;

import java.util.List;

public class LinkUtils {
    public static Link getLink(List<Link> links, String rel) {
        return links.stream()
                .filter(link -> link.getRel().equals(rel))
                .findFirst()
                .orElse(null);
    }
}
