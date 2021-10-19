package com.blackjack.rules;

import com.blackjack.cards.Card;
import com.blackjack.cards.CardDeck;
import com.blackjack.person.Dealer;
import com.blackjack.person.Gamer;

import java.util.Scanner;

public class Game {
    private static final int INIT_RECEIVE_CARD_COUNT = 2;

    // 게임에 필요한 인스턴스 생성
    public void play() {
        System.out.println("====Blackjack====");
        Scanner sc = new Scanner(System.in);

        Dealer dealer = new Dealer();
        Gamer gamer = new Gamer();
        Rule rule = new Rule();
        CardDeck cardDeck = new CardDeck();

//        System.out.println(cardDeck.toString());
//        System.out.println(cardDeck.draw());
        initPhase(cardDeck, gamer);
        playingPhase(sc, cardDeck, gamer);
    }

    private void playingPhase(Scanner sc, CardDeck cardDeck, Gamer gamer) {
        String gamerInput;
        while (true) {
            System.out.println("카드를 뽑겠습니까? 종료를 원하시면 0을 입력하세요.");
            gamerInput = sc.nextLine();

            if ("0".equals(gamerInput)) break;

            Card card = cardDeck.draw();  // 카드 뽑기
            gamer.receiveCard(card);  // 게이머가 카드 받고 확인
        }
    }

    private void initPhase(CardDeck cardDeck, Gamer gamer) {
        // 처음 시작할때 게이머가 2장의 카드를 받는 메서드
        System.out.println("처음 2장의 카드를 각자 뽑겠습니다.");
        for (int i = 0; i < INIT_RECEIVE_CARD_COUNT; i++) {
            Card card = cardDeck.draw();
            gamer.receiveCard(card);
        }
    }
}
