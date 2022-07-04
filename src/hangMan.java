import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class hangMan {
    public static void main(String[] args) {
        introToTheGame();
    }

    private static void introToTheGame() {
        System.out.println("Welcome to HANGMAN!");
        System.out.print("\nWould you like to Play? Y or N:  ");
        Scanner in = new Scanner(System.in);
        String play;
        while (true) {
            try {
                play = in.nextLine().toLowerCase();
                if (play.equals("n")) {
                    System.out.println("\nYOU DO NOT WANT TO PLAY!");
                    System.out.println("Program exiting...");
                    System.exit(0);
                } else if (play.equals("y")) {
                    System.out.println("\nThank you for choosing to play the game!");
                    playTheGame();
                }
                else{
                    System.out.println("Invalid Input! Please only type y or n");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid Input! Please only type y or n");
                in.nextLine(); //consume the input to prevent infinite loop
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void playTheGame() throws InterruptedException {
        String word = wordRandomizer();
        System.out.println("\n\nThe word consists of " + word.length() + " letters!");
        Scanner in = new Scanner(System.in);
        userGuessWord(word, in);

    }

    private static void userGuessWord(String word, Scanner in) throws InterruptedException {
        List<Character> wordList = word.chars().mapToObj(a -> (char) a).collect(Collectors.toList());
        StringBuilder strBuild = new StringBuilder();
        System.out.println(word);
        for (int i = 0; i < wordList.size(); i++){
            strBuild.append("_");
            System.out.print("_" + " ");
        }
        int counter = 0;
        while (counter < 6) {
            try {
                System.out.print("\n\nPlease choose a letter from a - z: ");
                String userGuesses = in.nextLine().toLowerCase();
                if(wordList.contains(userGuesses.charAt(0))){
                    System.out.println("\nWord contains " + userGuesses);
                    int x = wordList.indexOf(userGuesses.charAt(0));
                    strBuild.deleteCharAt(x);
                    strBuild.insert(x, userGuesses.charAt(0));

                    for(int i = 0; i < strBuild.toString().length(); i++){
                        System.out.print(strBuild.charAt(i) + " ");
                    }

                    if(strBuild.toString().equals(word)){
                        System.out.println("\n\nYou guessed the word!!!");
                        break;
                    }
                    continue;
                }
                else{
                    counter++;
                    int tryCounters = 6 - counter;
                    System.out.println("\nWord does not contain " + userGuesses + ". Please try again.");
                    System.out.println("You have " + tryCounters + " more tries");
                    System.out.println(strBuild);
                }
            }
            catch (InputMismatchException e){
                in.nextLine(); //consume the user input;
            }
        }

        if(counter == 6) {
            System.out.println("\nYOU LOST!!!!");
        }
        System.out.println("\nWould you like to play again? ");
        String playAgainString = playAgain(in); //play again prompt
        if(playAgainString.equals("Game Restarting...")){
            System.out.println(playAgainString);
            playTheGame();
        }
        else {
            System.out.println(playAgainString);
            System.exit(0);
        }

    }

    public static String playAgain(Scanner in) {
        String playAgain;

        while (true) {
            try {
                playAgain = in.nextLine().toLowerCase();
                if (playAgain.equals("n")) {
                    return "Thank you for playing!";
                } else if (playAgain.equals("y")) {
                    //playTheGame();
                    return "Game Restarting...";
                }
                else{
                    System.out.println("Invalid Input! Please only type y or n");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid Input! Please only type y or n");
                in.nextLine(); //consume the input to prevent infinite loop
            }
        }

    }


    private static String wordRandomizer() {
        List<String> strFiles = new ArrayList<>();
        try {
            strFiles = Files.lines(Paths.get("C:\\Users\\johna\\Downloads\\hangMan\\src\\randomWords.txt")).collect(Collectors.toList());
        }
        catch (IOException e){
            System.err.println("File does not exist");
            return "No Files Found";
        }

        Random rand = new Random();
        int randomGenerator = rand.nextInt(strFiles.size() - 1);
        return strFiles.get(randomGenerator);
    }
}
