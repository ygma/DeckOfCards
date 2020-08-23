package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@Lazy
public class HelloController {

    @GetMapping("/hello")
    public LinksResponse<String> hello() {
        return new LinksResponse<>("hello world", Collections.emptyList());
    }
}
