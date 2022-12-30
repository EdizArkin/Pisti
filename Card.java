public class Card {
    // The propertys of the cards are defined in this class.
    public String suit;
    public String value;
    public Card(String suit, String value){
        this.value = value;
        this.suit = suit;
    }
    public String getSuit() {
        return suit;
    }
    public void setSuit(String suit) {
        this.suit = suit;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String output = suit+value;
        return output;
    }

}