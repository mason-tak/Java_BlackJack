# 순수 자바로 블랙잭 프로젝트 만들어보기

> [이동욱님 github](https://github.com/jojoldu/oop-java) 을 보고 참고 하였으며 해당 프로젝트는 분석을 토대로 어떻게 구현하였는가에 대해서 알아볼 예정

객체지향을 연습하기 위해 시작하는 예제 프로젝트

## 게임 설명

### 게임 이름 : 블랙잭

자신이 받은 카드의 숫자 합이 21에 가까워야하고 그 숫자가 딜러 카드의 숫자 보다 높으면 이기는 게임이다.

### 게임 규칙

- 딜러와 게이머 단 2명 존재
- 카드는 조커를 제외한 52장이다. (카드는 다이아, 하트, 스페이드, 클로버 무늬를 가진 A,2~10,K,Q,J으로 이루어져 있다.)
- 2~13은 숫자 그대로 점수를 J,Q,K는 각 11, 12, 13점으로 A는 1로 계산한다. (기존 규칙은 A는 1과 11 둘다 가능하지만 여기선 1로만 허용)
- 딜러와 게이머는 순차적으로 카드를 하나씩 뽑아 각자 2개의 카드를 소지한다.
- 게이머는 얼마든지 카드를 추가로 뽑을 수 있다.
- 딜러는 2카드의 합계 점수가 16점 이하이면 반드시 1장을 추가로 뽑고, 17점 이상이면 추가 할 수 없다.
- 양쪽 다 추가 뽑기 없이 카드를 오픈 하면 딜러와 게이머 중 소유한 카드의 합이 21에 가장 가까운 쪽이 승리한다.
- 단 21을 초과하면 초과한 쪽이 진다.

## CardDeck 구현하기

해당 코드의 내용은 CardDeck에 있는 카드들을 생성하는 로직을 구현의 목적을 담고 있다.

`gitbranch - feature/carddeck_implement`

일단 실제로 봤을때도 그렇지만 카드덱에는 카드가 담겨 있어야한다.

그렇게 생각하면 CardDeck 클래스가 처음 호출되었을때 52장의 카드를 생성시켜줘야한다.

CardDeck 클래스를 호출할때 제일먼저 딱 한번 실행되는 생성자 부분에 로직을 작성해야한다.

일단 멤버변수에 static 변수로 patterns과 cardcount를 지정해준다.

그리고 List에 52개의 서로 다른 카드를 생성해야하기 때문에 로직을 구성해준다.

```java
import com.blackjack.cards.Card;

import java.util.ArrayList;

public class CardDeck {
    private List<Card> cards;
    private static final String[] PATTERNS = {"spade", "heart", "diamond", "club"};
    private static final int CARD_COUNT = 13;

    public CardDeck() {
        cards = new ArrayList<>();

        for (String pattern : PATTERNS) {
            for (int i = 1; i <= CARD_COUNT; i++) {
                Card card = new Card();
                String denomination;

                if (i == 1) denomination = "A";
                else if (i == 11) denomination = "J";
                else if (i == 12) denomination = "Q";
                else if (i == 13) denomination = "K";
                else denomination = String.valueOf(i);

                card.setDenomination(denomination);
                card.setPattern(pattern);
                cards.add(card);
            }
        }
    }
}
```

위와 같이 코드를 구성하긴 했지만 객체지향에 맞지 않게 구성한거 같다.

일단, 생성자는 실제 비지니스 로직을 알고 있을 필요도 없고 객체지향 설계의 5원칙중 SRP 단일 책임 원칙에 의하면

각 메서드는 하나의 역할에만 충실해야한다는 내용이 있다.

그리고 생성자의 특징중 하나가 인스턴스 변수의 초기화가 목적인 점을 보아 위의 코드는 로직 구성에 맞지 않다고 볼 수 있다.

그러면 위의 코드를 각자의 역할을 담당하게끔 나눠 볼 수 있다.

```java
public class CardDeck {
    private List<Card> cards;
    private static final String[] PATTERNS = {"spade", "heart", "diamond", "club"};
    private static final int CARD_COUNT = 13;

    public CardDeck() {
        cards = this.generateCards();
    }

    private List<Card> generateCards() {
        // 카드 리스트를 생성하는 곳
        List<Card> cards = new LinkedList<>();

        for (String pattern : PATTERNS) {
            for (int i = 1; i <= CARD_COUNT; i++) {
//                Card card = new Card();
//                card.setDenomination(denomination);
//                card.setPattern(pattern);
                String denomination = this.numberToDenomination(i);
                Card card = new Card(pattern, denomination); // 카드는 끗수와 무늬가 필수임을 강제할 수 있다.
                cards.add(card);
            }
        }
        return cards;
    }

    private String numberToDenomination(int number) {
        // 카드의 끗수를 정하는 곳
        if (number == 1) return "A";
        else if (number == 11) return "J";
        else if (number == 12) return "Q";
        else if (number == 13) return "K";
        return String.valueOf(number);
        // String.valueOf()를 사용한 이유 : 파라미터가 null일 경우 null을 반환한다.
        // toString()을 사용할 경우 NullPointerException 에러가 발생한다.
    }
}
```

추가적인 내용은 Card 클래스의 set메서드를 빼고 파라미터를 가진 Card 클래스의 생성자를 사용했다.

그렇게 한다면 denomination(끗수)와 pattern(무늬)를 Card 클래스에서 마음대로 활용한다 해도 CardDeck 클래스에서는 아무런 영향이 없기 때문이다.

그리고 Card 클래스를 호출할때 끗수와 무늬는 필수임을 알 수 있게 해준다. (둘 중에 하나라도 없다면 생성 될 수 없다.)

`gitbranch - feature/carddeck_implement`

카드를 생성했다면 카드를 뽑는 로직과 뽑은 카드를 삭제하는 로직을 구성해야한다.

```java
// CardDeck class 아래 메서드를 추가 한다.

public Card draw(){
        int size=cards.size();
        int selelct=(int)(Math.random()*size());
        Card selectedCard=cards.get(select);
        cards.remove(select);
        return selectedCard;
        }
```

일단 문제점은 하나의 메서드에서 두가지 작업을 한다는 점이다.

카드 뽑기 & 뽑은 카드를 제거

여기서 카드 뽑기는 private으로 설정해서 해당 클래스이외에는 접근할 수 없게 작성한다. (이유 : 다른 클래스에서도 카드를 뽑을 수 있게 되면 CardDeck이라는 기능이 필요 없어지기 때문에)

```java
public Card draw(){
        Card selectedCard=getRandomCard();
        cards.remove(select);  // 뽑은 카드 삭제
        return selectedCard;
        }

public Card getRandomCard(){
        int size=cards.size();
        int select=(int)(Math.random()*size);
        return cards.get(select);
        }
```

## Gamer 구현하기

**Gamer의 역할**

- 추가로 카드를 받는다.
- 뽑은 카드를 소유한다.
- 카드를 오픈한다.

receiveCard라는 메서드를 생성해 뽑은 카드를 소유할 수 있도록 해준다.

카드를 저장할 요소는 ArrayList이다.

그리고 게이머는 현재 본인의 카드의 총 점수가 몇점인지 알아야한다.

그래야 카드를 더 뽑을지 안뽑을지 결정할 수 있기 때문이다.

소유 카드의 목록을 확인하는 메서드(showCards)를 생성한다.

그리고 뽑은 카드를 소유할때 확인도 가능해야하니 receiveCard()에서 showCards() 메서드를 호출해주자

```java
public class Gamer {
    private List<Card> cards;
    int numberGames = 0;

    public Gamer() {
        cards = new ArrayList<>();  // 뽑은 카드를 담을 곳
    }

    public void receiveCard(Card card) {
        // 뽑은 카드 소유
        this.cards.add(card);
        // 카드를 받을때 현재 소유한 카드를 확인해야함
        this.showCards();
    }

    public void showCards() {
        StringBuilder sb = new StringBuilder();
        sb.append("현재 보유 카드 목록 \n");

        for (Card card : cards) {
            sb.append(card.toString());
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

    public List<Card> openCards() {
        // 카드 오픈
        // 현재 가지고 있는 모든 카드들을 전달하는 역할
        return this.cards;
    }
}
```

그럼 Game class에서 카드를 뽑는 메서드를 생성해야한다.

해당 메서드의 내용은 CardDeck을 통해 카드를 뽑고 Gamer가 그 카드를 받고 현재 카드를 확인하는 내용의 로직을 작성해준다. (playingPhase())

그리고 initPhase() 메서드는 처음 게임을 시작할때 딜러와 게이머가 각 2장씩 받는 기능을 할 메서드이다.

마찬가지로 카드를 뽑고 확인 하는 메서드를 호출해준다.

```java
public class Game {
    ...
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
```

