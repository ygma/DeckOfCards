# DeckOfCards

## Prerequisite

- java 1.8 & maven cli

or

- docker & docker-compose

or

- IDE

## How to run

- maven
```
mvn clean package
java -jar RestfulApi/target/restfulapi.jar
```

- docker
```
docker-compose up -d deck-of-cards
```
Then have to wait for half minute to startup

After run, shut down the container
```
docker-compose down
```

- IDE

This is developed in Intellij and java 8, but it is supposed to be run by any java IDE.
(But I enabled lombok, sometimes it's a bit difficult to configure it in a IDE. )

All the unit tests are under `RestfulApi` module.

Run `RestfulApi` module will start a http server to listen to http requests

- Test

Send requests to `localhost:8080` in any way

## API Specification

- Create a game
```
POST /games
```
- Delete a game
```
DELETE /games/{id}
```
- Create a deck
```
POST /decks
```
- Add a deck to a game deck
```
POST /games/{id}/decks
```
- Add a player to the game
```
POST /games/{id}/players
```
- Delete a player from a game
```
DELTE /games/{id}/players/{id}
```
- Deal cards to a player in a game from the game deck
```
POST /games/{id}/players/{id}/cards
```
- Get the list of cards for a player
```
GET /games/{id}/players/{id}/cards
```
- Get the list of players in a game along with the total added value 
```
GET /games/{id}/players
```
- Get the count of how many cards per suit are left
```
GET /games/{id}/cards/undealt/suits
```
- Get the count of each card (suit and value) remaining in the game deck
```
GET /games/{id}/cards/undealt/suits/values
```
- Shuffle the game deck
```
POST /games/{id}/cards/undealt/shuffle
```

## HATEOAS Driven REST APIs

The homework is implemented based on HATEOAS (Hypermedia as the Engine of Application State).

REST architectural style lets us use the hypermedia links in the response contents. It allows the client can dynamically
 navigate to the appropriate resources by traversing the hypermedia links.

E.g. When the client call api to create a new game, all the possible operations (APIs) would be returned in the `links` 
fields. 

Same as player API.

```
{
    "data": {
        "id": "b73b8518-753d-4e64-b71e-34e554cc5ff7"
    },
    "links": [
        {
            "href": "/games/b73b8518-753d-4e64-b71e-34e554cc5ff7",
            "rel": "delete game",
            "type": "DELETE"
        },
        {
            "href": "/games/b73b8518-753d-4e64-b71e-34e554cc5ff7/players",
            "rel": "add player to game",
            "type": "POST"
        },
        {
            "href": "/games/b73b8518-753d-4e64-b71e-34e554cc5ff7/players",
            "rel": "list players",
            "type": "GET"
        },
        {
            "href": "/games/b73b8518-753d-4e64-b71e-34e554cc5ff7/cards/undealt/suits",
            "rel": "undealt cards per suit",
            "type": "GET"
        },
        {
            "href": "/games/b73b8518-753d-4e64-b71e-34e554cc5ff7/cards/undealt/suits/values",
            "rel": "undealt card count by suit and value",
            "type": "GET"
        },
        {
            "href": "/games/b73b8518-753d-4e64-b71e-34e554cc5ff7/cards/undealt/shuffle",
            "rel": "shuffle",
            "type": "POST"
        },
        {
            "href": "/games/b73b8518-753d-4e64-b71e-34e554cc5ff7/decks",
            "rel": "add deck to game",
            "type": "POST"
        }
    ]
}
```

## Trade-offs

Due to homework effort and limited time, I made the following trade-offs during the homework

- Unit test coverage is not full enough. Only test simple cases and happy path.
- Didn't introduce a real database, but only temporary in-memory storage. But the DAO interfaces should be good and ready
 to move to relational db or no-sql db in the future. 