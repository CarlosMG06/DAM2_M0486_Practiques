package com.project.exemples;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// Enum per representar els pals de la baralla
enum Suit {
    SPADES,
    CLUBS,
    HEARTS,
    DIAMONDS
}

// Classe que representa una carta individual
class Card {
    final char rank; // El rang de la carta (ex: 'A', 'K', '6')
    final Suit suit; // El pal de la carta

    public Card(char rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }
}

// Classe que representa una mà de Blackjack
class BlackjackHand {
    final Card hidden_card;       // La carta que està tapada
    final List<Card> visible_cards; // La llista de cartes visibles

    public BlackjackHand(Card hidden_card, List<Card> visible_cards) {
        this.hidden_card = hidden_card;
        this.visible_cards = visible_cards;
    }
}

public class ExempleMoshi {
    public static void main(String[] args) throws IOException {
        // Creem la mà de Blackjack amb una carta tapada i dues de visibles
        BlackjackHand blackjackHand = new BlackjackHand(
            new Card('6', Suit.SPADES),
            Arrays.asList(new Card('4', Suit.CLUBS), new Card('A', Suit.HEARTS))
        );

        // Iniciem Moshi
        Moshi moshi = new Moshi.Builder().build();

        // Demanem a Moshi un adaptador per a la classe BlackjackHand
        JsonAdapter<BlackjackHand> jsonAdapter = moshi.adapter(BlackjackHand.class);

        // Convertim l'objecte blackjackHand a un string JSON
        String json = jsonAdapter.toJson(blackjackHand);

        // Imprimim el resultat per consola
        System.out.println(json);
    }
}