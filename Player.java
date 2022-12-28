import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private Card[] hand;
    private String playerName;
    private int score;
    private List<Card> collectedCardList;

    public Player(String name){
        this.playerName = name;
        this.collectedCardList = new ArrayList<Card>();
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
    public String showMyHand(){
        return Arrays.toString(this.hand);
    }
    public List<Card> getCollectedCardList() {
        return collectedCardList;
    }
    public void setCollectedCardList(List<Card> collectedCardList) {
        this.collectedCardList = collectedCardList;
    }

    // This function counts the points of the cards that the player collects in his inventory.
    public int calculatePlayerScore(){
        int result = 0;
        for(Card card : collectedCardList){
            if(card.getSuit().equals("♦") && card.getValue().equals("10")){
                result=result+3;
            }
            else if(card.getSuit().equals("♣") && card.getValue().equals("2")){
                result=result+2;
            }else{
                result=result+1;
            }
        }
        result = result + this.score;
        return result;
    }
}