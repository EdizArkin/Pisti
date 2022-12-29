public class PistiApplication {
    // it's just the main method that starts the game.
    public static void main(String[] args) {
        System.out.println("♠ ♣ ♥ ♦ ---Welcome to the PISTI Game--- ♦ ♥ ♣ ♠");
        System.out.println(" ");
        CardOperation op = new CardOperation();
        Card[] cardArray = op.prepareDeckForNewGame();
        Game newGame = new Game();
        newGame.playGame(cardArray);
    }
}
