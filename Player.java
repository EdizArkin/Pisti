import java.util.Arrays;

public class Player {
    /*
    This class contains the player's properties
     and the function by which the cards he will win are calculated.
     */
    private Card[] hand;
    private String playerName;
    private int score;
    private Card[] collectedCards;

    public Player(String name) {
        this.playerName = name;
        collectedCards = new Card[52];
    }
    public Card[] getHand() {
        return hand;
    }
    public void setHand(Card[] hand) {
        this.hand = hand;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String showMyHand() {
        return Arrays.toString(this.hand);
    }
    public Card[] getCollectedCards() {
        return collectedCards;
    }
    public void setCollectedCards(Card[] collectedCards) {
        this.collectedCards = collectedCards;
    }

    // This function counts the points of the cards that the player collects in his inventory
    public int calculatePlayerScore() {
        int result = 0;
        for (Card card : this.collectedCards) {
            if (card != null) {
                if (card.getSuit().equals("♦") && card.getValue().equals("10")) {
                    result = result + 3;
                } else if (card.getSuit().equals("♣") && card.getValue().equals("2")) {
                    result = result + 2;
                } else {
                    result = result + 1;
                }
            }

        }
        System.out.println("Score of collected cards : " + result);
        result = result + this.score;
        return result;
    }
}