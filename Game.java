import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    // Calling the necesery objects to start the game.
    Player myPlayer;
    Player computerPlayer = new Player("COMPUTER");
    Card[] cardsOnBoards = new Card[52];
    int lastPlayerNumber = 0;

    /* This part is the main part that will play the game,
    and other functions will be called here.
     */
    public void playGame(Card[] cardArray){
        // The first 4 cards are discarded and the last card is shown.
        cardsOnBoards = CardOperation.addAll(cardsOnBoards, Arrays.copyOfRange(cardArray, 0, 4));
        myPlayer = PlayerOperation.createMyPlayer();
        System.out.println("Before first move, cards on the table:"+cardsOnBoards[3]);
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
        if(lastPlayerNumber==1){
            myPlayer.setCollectedCards(CardOperation.addAll(myPlayer.getCollectedCards(), cardsOnBoards));
        }else{
            computerPlayer.setCollectedCards(CardOperation.addAll(computerPlayer.getCollectedCards(), cardsOnBoards));
        }
        calculateFinalScores();
    }

    /*
    This function allows the player to discard one of the cards in his hand,
    and calls the function that will check whether
    the discarded card and the card on the table are mached or pişti.
     */
    public void playTheHand(){
        int score = 0;
        for(int i = 0; i<4; i++){
            Card card = chooseCard();
            System.out.println("your chosen card : " + card.toString());
            cardsOnBoards[CardOperation.getEmptyIndexofArray(cardsOnBoards)] = card;
            Integer[] evaluation =(Integer[]) evaluateTheHand(card);
            score = evaluation[1];
            if(evaluation[0] > 0){//checkWin == 1
                myPlayer.setCollectedCards(CardOperation.addAll(myPlayer.getCollectedCards(), cardsOnBoards));
                cardsOnBoards = new Card[52];
                myPlayer.setScore(myPlayer.getScore() + score);
            }
            score = 0;
            Card compCard = chooseForComputer();
            System.out.println("computer's chosen card : "+compCard.toString());
            cardsOnBoards[CardOperation.getEmptyIndexofArray(cardsOnBoards)] = compCard;
            evaluation =(Integer[]) evaluateTheHand(compCard);
            score = evaluation[1];
            if(evaluation[0] > 0){
                computerPlayer.setCollectedCards(CardOperation.addAll(computerPlayer.getCollectedCards(), cardsOnBoards));
                cardsOnBoards = new Card[52];
                computerPlayer.setScore(computerPlayer.getScore()+score);
            }
            System.out.print("MyPlayer's collection : ");
            CardOperation.printArrayWithoutNull(myPlayer.getCollectedCards());
            System.out.print("Computer's collection : ");
            CardOperation.printArrayWithoutNull(computerPlayer.getCollectedCards());
        }
    }

    /* This function allows the player to choose one of the card in his hand
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
                if(0 < index && index < 5 && index<=myPlayer.getHand().length){
                    chosenCard = myPlayer.getHand()[index-1];
                    myPlayer.setHand(CardOperation.removeCardFromHand(myPlayer.getHand(), (index-1)));
                    break;
                }
                else
                    System.out.println("Please enter a valid index");
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
        int size = CardOperation.getEmptyIndexofArray(cardsOnBoards);
        Card chosenCard = null;
        if(size>0){
            // Computer will theow cards according to the top card on the table.
            Card lastCardOnBoard = cardsOnBoards[CardOperation.getEmptyIndexofArray(cardsOnBoards)-1];
            for(int i=0; i<computerPlayer.getHand().length; i++){
                // If the value of the computer's card matches the value of the card on the desk and the card unused.
                chosenCard = computerPlayer.getHand()[i];
                if(chosenCard.getValue().equals(lastCardOnBoard.getValue())){
                    computerPlayer.setHand(CardOperation.removeCardFromHand(computerPlayer.getHand(), i));
                    return chosenCard;
                }
            }
            for(int i=0; i<computerPlayer.getHand().length; i++){
                // If there is no matching card on the table, Jack it.
                chosenCard = computerPlayer.getHand()[i];
                if(chosenCard.getValue().equals("J")){
                    computerPlayer.setHand(CardOperation.removeCardFromHand(computerPlayer.getHand(), i));
                    return chosenCard;
                }
            }
            // Will discard the first unused card.
            if(computerPlayer.getHand().length>0){
                chosenCard = computerPlayer.getHand()[0];
                computerPlayer.setHand(CardOperation.removeCardFromHand(computerPlayer.getHand(), 0));
                return chosenCard;
            }

        }else if(computerPlayer.getHand().length>0){
            chosenCard = computerPlayer.getHand()[0];
            computerPlayer.setHand(CardOperation.removeCardFromHand(computerPlayer.getHand(), 0));
            return chosenCard;
        }
        lastPlayerNumber = 1;
        return chosenCard;
    }

    /* This function checks whether the cards discarded by the players match the cards on the table,
    and assigns the collected cards to the players collection
     */
    public Object[] evaluateTheHand(Card card){
        Integer[] result = new Integer[2];
        //result[0] = win=1, pass=0
        //result[1] = score
        int score = 0;
        int checkWin = 0;

        if(CardOperation.getEmptyIndexofArray(cardsOnBoards) > 1){
            // Pıstı Condition
            if(CardOperation.getEmptyIndexofArray(cardsOnBoards)==2 && card.value.equals(cardsOnBoards[0].value)){
                score=10;
                checkWin = 1;
            }else if(cardsOnBoards[CardOperation.getEmptyIndexofArray(cardsOnBoards)-2]!=null
                    &&((cardsOnBoards[CardOperation.getEmptyIndexofArray(cardsOnBoards)-2].getValue().equals(card.getValue())) ||
                    (card.getValue().equals("J")))){
                checkWin = 1;
            }
        }

        System.out.println("CARDS ON BOARD : ");
        System.out.println("___________________________");
        CardOperation.printArrayWithoutNull(cardsOnBoards);
        System.out.println("___________________________");
        System.out.println(" ");

        result[0] = checkWin;
        result[1] = score;
        return result;
    }

    /* This function calculates the final scores to
      determinate the endgame winner, and displays the winner.
     */
    public void calculateFinalScores(){
        System.out.println("-----------------------------------");
        System.out.println("MyPlayer's final cards : ");
        CardOperation.printArrayWithoutNull(myPlayer.getCollectedCards());
        System.out.println("Computer's final cards : ");
        CardOperation.printArrayWithoutNull(computerPlayer.getCollectedCards());

        System.out.println("Players SCORES : ");
        System.out.println("PİŞTİ Score : " + myPlayer.getScore());
        int playerScore = myPlayer.calculatePlayerScore();
        System.out.println(" ");
        System.out.println("Computers SCORES :");
        System.out.println("PİŞTİ Score : " + computerPlayer.getScore());
        int computerScore = computerPlayer.calculatePlayerScore();
        System.out.println(" ");
        int winnerScore = 0;

        System.out.println("Winner is ... ");
        if(playerScore>computerScore){
            winnerScore = playerScore;
            System.out.println(myPlayer.getPlayerName() + " score : " + winnerScore);
            checkScoreBoard(winnerScore);
        }else{
            winnerScore = computerScore;
            System.out.println(computerPlayer.getPlayerName() + " score:" + winnerScore);
            checkScoreBoard(winnerScore);
        }
    }

    /*
    Under this function, there is a string of code that prints the player's score to a list of 10 players,
     respectively, if the player has won each time the game is played.
     */
    public void checkScoreBoard(int winnerScore){
        String[][] scoreBoard = new String[10][2];
        File f = new File("scoreboard.txt");

        if(f.isFile()) {
            int i = 0;
            try {
                Scanner scanner = new Scanner(f);
                while (scanner.hasNextLine() && i<10) {
                    String line = scanner.nextLine();
                    System.out.println(line);
                    if(line!= null || line!=""){
                        String[] rawLine = line.split(" ");
                        scoreBoard[i][0] = rawLine[0];//name
                        scoreBoard[i][1] = rawLine[1];//score
                        i = i + 1;
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        String[][] sortedScoreboard = new String[10][2];
        // if Scroreboard is empty.
        if(scoreBoard[0][0]==null){
            sortedScoreboard[0][0] = myPlayer.getPlayerName();
            sortedScoreboard[0][1] = winnerScore+"";
        }else{
            for(int i = 0; i<scoreBoard.length; i++){
                if(scoreBoard[i][1]!= null){
                    String x = scoreBoard[i][1];
                    if(winnerScore>Integer.parseInt(scoreBoard[i][1])){
                        sortedScoreboard[i][0] = myPlayer.getPlayerName();
                        sortedScoreboard[i][1] = winnerScore+"";
                        if(scoreBoard[i][0] != null){
                            sortedScoreboard[i+1][0] = scoreBoard[i][0];
                            sortedScoreboard[i+1][1] = scoreBoard[i][1];
                        }

                        i = i + 1;
                    }else if(scoreBoard[i][1] == null){
                        sortedScoreboard[i][0] = scoreBoard[i][0];
                        sortedScoreboard[i][1] = scoreBoard[i][1];
                    }else{
                        if(scoreBoard[i][0] != null){
                            sortedScoreboard[i][0] = scoreBoard[i][0];
                            sortedScoreboard[i][1] = scoreBoard[i][1];
                        }
                    }
                }

            }
        }

        // Write scores.
        File fout = new File("scoreboard.txt");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < 10; i++) {
                if(sortedScoreboard[i][0]!=null){
                    bw.write(sortedScoreboard[i][0]+" "+sortedScoreboard[i][1]);
                    bw.newLine();
                }
            }
            bw.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
