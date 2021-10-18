package com.blackjack.rules;

import com.blackjack.cards.CardDeck;
import com.blackjack.person.Dealer;
import com.blackjack.person.Gamer;

public class Game {
    // 게임에 필요한 인스턴스 생성
    public void play() {
        System.out.println("====Blackjack====");
        Dealer dealer = new Dealer();
        Gamer gamer = new Gamer();
        Rule rule = new Rule();
        CardDeck cardDeck = new CardDeck();
    }
}
