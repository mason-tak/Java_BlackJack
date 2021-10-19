package com.blackjack.cards;

// 카드
public class Card {
    private String pattern;  // 무늬
    private String denomination;  // 끗수 (A, 2~10, J,Q,K)

    public Card(String pattern, String denomination) {
        this.pattern = pattern;
        this.denomination = denomination;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    @Override
    public String toString() {
        return "Card { " + "pattern = " + pattern + ", denomination = " + denomination + "}";
    }
}
