import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {

    // Calling the necessary objects to start the game
    Player myPlayer;
    Player computerPlayer = new Player("COMPUTER");
    List<Card> cardsOnBoard = new ArrayList<Card>();
    int lastPlayerNumber = 0;


    /*This part is the main part that will play the game,
    and other functions will be called here
     */
    public void playGame(Card[] cardArray){
        // The first 4 cards are discarded and the last card is shown.
        cardsOnBoard.addAll(Arrays.asList(Arrays.copyOfRange(cardArray, 0, 4)));
        //System.out.println("Before first move, cards on the table : " + cardsOnBoard.get(3));
        myPlayer = PlayerOperation.createMyPlayer();
        System.out.println("Before first move, cards on the table : " + cardsOnBoard.get(3));
        int round = 1;
        for(int i=4; i<52; i=i+8){
            System.out.println("_________ROUND " + round + "_________");
            Card[] firstHand = Arrays.copyOfRange(cardArray, i, i+4);
            myPlayer.setHand(firstHand);

            Card[] secondHand = Arrays.copyOfRange(cardArray, i+4, i+8);
            computerPlayer.setHand(secondHand);
            playTheHand();
            round += 1;
        }
        // In final round if there is a card left on the table, it will be given to the last player.
        if(lastPlayerNumber == 1){
            myPlayer.getCollectedCardList().addAll(cardsOnBoard);
        }
        else{
            computerPlayer.getCollectedCardList().addAll(cardsOnBoard);
        }
        calculateFinalScores();
    }


    /*
    This function allows the player to discard one of the cards in his hand,
    and calls the function that will check whether
    the discarded card and the card on the table are matched or Pişti.
     */
    public void playTheHand(){
        int score = 0;
        for(int i = 0; i<4; i++){
            Card card = chooseCard();
            System.out.println("your chosen card : " + card.toString());
            cardsOnBoard.add(card);
            Integer[] evaluation =(Integer[]) evaluateTheHand(card);
            score = evaluation[1];
            if(evaluation[0] > 0){ //checkWin == 1
                myPlayer.getCollectedCardList().addAll(cardsOnBoard);
                cardsOnBoard.clear();
                myPlayer.setScore(myPlayer.getScore() + score);
            }
            score = 0;
            Card compCard = chooseForComputer();
            System.out.println("computer's chosen card : " + compCard.toString());
            cardsOnBoard.add(compCard);
            evaluation = (Integer[]) evaluateTheHand(compCard);
            score = evaluation[1];
            if(evaluation[0]>0){
                computerPlayer.getCollectedCardList().addAll(cardsOnBoard);
                cardsOnBoard.clear();
                computerPlayer.setScore(computerPlayer.getScore() + score);
            }
        }
    }


    /* This function allows the player to choose one of the cards in his hand
    and prevents index errors by re-indexing the remaining cards.
     */
    public Card chooseCard(){
        System.out.println("your hand : " + myPlayer.showMyHand());
        if(myPlayer.getHand().length>1){
            System.out.println("Write the index of your cards between 1-" + myPlayer.getHand().length + " : ");
        }
        else{
            System.out.println("Please enter 1 : ");
        }

        Scanner scanner = new Scanner(System.in);
        Card chosenCard = null;
        int index = 0;
        while (true) {
            try {
                index = Integer.valueOf(scanner.nextLine());
                if(0 < index && index < 5 && index <= myPlayer.getHand().length){
                    chosenCard = myPlayer.getHand()[index-1];
                    myPlayer.setHand(CardOperation.removeCardFromHand(myPlayer.getHand(), (index-1)));
                    break;
                }
                else{
                    System.out.println("Please enter a valid index");}
            }
            catch (Exception e) {
                System.out.println("Please enter a valid index");
            }
        }
        lastPlayerNumber = 1;
        return chosenCard;
    }


    // This function includes the artificial intelligence of the computer.
    public Card chooseForComputer(){
        lastPlayerNumber = 2;
        int size = cardsOnBoard.size();
        Card chosenCard = null;
        if(size > 0){
            // Computer will throw cards according to the top card on the table.
            Card lastCardOnBoard = cardsOnBoard.get(size-1);

            for(int i = 0; i<computerPlayer.getHand().length; i++){
                // If the value of the computer's card matches the value of the card on the desk and the card unused.
                chosenCard = computerPlayer.getHand()[i];
                if(chosenCard.getValue().equals(lastCardOnBoard.getValue())){
                    computerPlayer.setHand(CardOperation.removeCardFromHand(computerPlayer.getHand(), i));
                    return chosenCard;
                }
            }
            for(int i = 0; i<computerPlayer.getHand().length; i++){
                // If there is no matching card on the table, jack it
                chosenCard = computerPlayer.getHand()[i];
                if(chosenCard.getValue().equals("J")){
                    computerPlayer.setHand(CardOperation.removeCardFromHand(computerPlayer.getHand(), i));
                    return chosenCard;
                }
            }
            // Will discard the first unused card
            if(computerPlayer.getHand().length > 0){
                chosenCard = computerPlayer.getHand()[0];
                computerPlayer.setHand(CardOperation.removeCardFromHand(computerPlayer.getHand(), 0));
                return chosenCard;
            }

        }else if(computerPlayer.getHand().length > 0){
            chosenCard = computerPlayer.getHand()[0];
            computerPlayer.setHand(CardOperation.removeCardFromHand(computerPlayer.getHand(), 0));
            return chosenCard;
        }
        lastPlayerNumber = 1;
        return chosenCard;
    }


    /* This function checks whether the cards discarded by the players match the cards on the table,
       and assigns the collected cards to the players' collection.
     */
    public Object[] evaluateTheHand(Card card){
        Integer[] result = new Integer[2];
        //result[0] = win=1, pass=0
        //result[1] = score
        int score = 0;
        int checkWin = 0;

        if(cardsOnBoard.size()>1){
            // Pıstı condition
            if(cardsOnBoard.size() == 2 && card.value.equals(cardsOnBoard.get(0).value)){
                score=10;
                checkWin = 1;
            }
            else if((cardsOnBoard.get(cardsOnBoard.size()-2).getValue().equals(card.getValue())) ||
                    (card.getValue().equals("J"))){
                checkWin = 1;
            }
        }
        System.out.println("myPlayer collected list : "+myPlayer.getCollectedCardList().toString());
        System.out.println("computer collected list : "+computerPlayer.getCollectedCardList().toString());
        System.out.println(" ");
        System.out.println("cardsOnBoard : "+cardsOnBoard.toString());

        result[0] = checkWin;
        result[1] = score;
        return result;
    }

    /* This function calculates the final scores to
     determine the endgame winner, and displays the winner.
     */
    public void calculateFinalScores(){
        int playerScore = myPlayer.calculatePlayerScore();
        int computerScore = computerPlayer.calculatePlayerScore();
        int winnerScore = 0;

        System.out.println("Winner is ... ");
        if(playerScore>computerScore){
            winnerScore = playerScore;
            System.out.println(myPlayer.getPlayerName() + " score : " + winnerScore);
            checkScoreBoard(winnerScore);
        }
        else{
            winnerScore = computerScore;
            System.out.println(computerPlayer.getPlayerName() + " score : " + winnerScore);
        }
    }

    // Test scoreList
    public void checkScoreBoard(int winnerScore){
        /*FileReader reader;
        try {
            reader = new FileReader("Scoreboard.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }*/
    }
}
