package com.blackjack.person;

import com.blackjack.cards.Card;

import java.util.List;

public interface Player {
    void receiverCard(Card card);

    void showCards();

    List<Card> openCards();
}
