import java.util.InputMismatchException;
import java.util.Scanner;
public class PlayerOperation {
    // The purpose of this class is to create characters for each new player who will play the game.
    public static Player createMyPlayer(){
        System.out.println("Please enter your name:");
        Scanner scanner = new Scanner(System.in);
        String name="";
        try {
            name = scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid name");
        }
        return new Player(name);
    }

}
