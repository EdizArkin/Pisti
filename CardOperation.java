import java.util.Random;
public class CardOperation {
    public final String[] suits = {"♠","♣","♥","♦"};
    public final String[] values = {"A","2","3","4","5","6","7","8","9","10","J","K","Q"};

    // This method creates the deck for the new game and sends it to the shuffle and cut function.
    public Card[] prepareDeckForNewGame(){
        Card[] cardArray = generateNewDeck();
        return shuffleAndCutTheDeck(cardArray);
    }

    // Cards are defined in this function.
    public Card[] generateNewDeck(){
        Card[] cardArray = new Card[52];
        int index = 0;
        for(String suit : suits){
            for(String value : values){
                Card card = new Card(suit,value);
                cardArray[index] = card;
                index = index + 1;
            }
        }
        return cardArray;
    }

    // Cards are shuffled and cut under this function.
    public Card[] shuffleAndCutTheDeck(Card[] originalArray){
        //Shuffle
        Card[] shuffledArray = new Card[originalArray.length];
        Random randomIndex = new Random();
        int originalArrayIndex = 0;
        int addedCardNumber = 0;
        while(addedCardNumber < 52){
            int shuffledArrayIndex = randomIndex.nextInt(52);
            if(shuffledArray[shuffledArrayIndex] == null){
                shuffledArray[shuffledArrayIndex] = originalArray[originalArrayIndex];
                originalArrayIndex = originalArrayIndex + 1;
                addedCardNumber = addedCardNumber + 1;
            }
        }
        //System.out.println("shuffledArray::"+Arrays.toString(shuffledArray));

        //Cut the deck
        int randomIndexForCutter = randomIndex.nextInt(52);
        //System.out.println("randomIndexForCutter:"+randomIndexForCutter);
        Card[] shuffledAndCutArray = new Card[originalArray.length];
        System.arraycopy(shuffledArray, 0, shuffledAndCutArray, shuffledArray.length-randomIndexForCutter, randomIndexForCutter);
        System.arraycopy(shuffledArray, randomIndexForCutter, shuffledAndCutArray, 0, (shuffledArray.length-randomIndexForCutter));
        //System.out.println("shuffledAndCutArray::"+Arrays.toString(shuffledAndCutArray));
        return shuffledAndCutArray;
    }



    // This function allows the player to discard the cards in her hand from hand to table.
    public static Card[] removeCardFromHand(Card[] originalHand, int index){
        Card[] resultHand = new Card[originalHand.length-1];
        int indexOfResult = 0;
        for(int i=0; i<originalHand.length; i++){
            if(i != index){
                //System.out.println("Card::"+originalHand[i].toString());
                resultHand[indexOfResult] = originalHand[i];
                indexOfResult = indexOfResult + 1;
            }
        }

        return resultHand;
    }
}