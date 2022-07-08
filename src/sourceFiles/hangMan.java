

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static void playTheGame() throws InterruptedException {
        String word = wordRandomizer();
        System.out.println("\n\nThe word consists of " + word.length() + " letters!");
        Scanner in = new Scanner(System.in);
        userGuessWord(word, in);

    }

    public static void userGuessWord(String word, Scanner in) throws InterruptedException {
        List<Character> characterList = word.chars().mapToObj(a -> (char) a).toList();
        List<Character> usedLetters = new ArrayList<>();
        StringBuilder strBuild = new StringBuilder();
        int counter = 0;
        hangManTemplate(counter);
        System.out.print("Guessed Letter : ");
        for (int i = 0; i < characterList.size(); i++){
            strBuild.append("_");
            System.out.print("_" + " ");
        }

        while (counter < 6) {
            try {
                System.out.println("\nUsed Letters: " + usedLetters);
                System.out.print("\n\nPlease choose a letter from a - z: ");
                String userGuesses = in.nextLine().toLowerCase();
                if(!usedLetters.contains(userGuesses.charAt(0))) { //if a letter is already used then we cant reuse it
                    if (characterList.contains(userGuesses.charAt(0))) { //if the user guessed letter is in the word then do this
                        hangManTemplate(counter);
                        System.out.println("\nWord contains " + userGuesses + "\n"); //prompt

                        List<Integer> indeces = IntStream.iterate(word.indexOf(userGuesses.charAt(0)), a -> a >= 0, a -> word.indexOf(userGuesses.charAt(0), a + 1))
                                .boxed().toList();
                        for (int temp : indeces) {
                            strBuild.deleteCharAt(temp);
                            strBuild.insert(temp, userGuesses.charAt(0));
                        }
                        usedLetters.add(userGuesses.charAt(0));
                        System.out.print("Guessed Letter : ");
                        for (int i = 0; i < strBuild.toString().length(); i++) {
                            System.out.print(strBuild.charAt(i) + " ");
                        }

                        if (strBuild.toString().equals(word)) {
                            System.out.println("\n\nYou guessed the word!!!");
                            System.out.println("The word is : " + strBuild);
                            break;
                        }

                    } else {
                        counter++;
                        int tryCounters = 6 - counter;
                        usedLetters.add(userGuesses.charAt(0));
                        hangManTemplate(counter);
                        if(counter == 6){
                            System.out.println("\nWord does not contain " + userGuesses +"! You're dead.");
                            break;
                        }
                        System.out.println("\nWord does not contain " + userGuesses + ". Please try again.");
                        System.out.println("You have " + tryCounters + " more tries");
                        System.out.print("Guessed Letter : ");
                        for (int i = 0; i < strBuild.toString().length(); i++) {
                            System.out.print(strBuild.charAt(i) + " ");
                        }
                    }
                }
                else{
                    System.out.println("Error: Letters cannot be used twice!");
                    System.out.print("Guessed Letter : ");
                    for (int i = 0; i < strBuild.toString().length(); i++) {
                        System.out.print(strBuild.charAt(i) + " ");
                    }
                    System.out.println();

                }
            }
            catch (InputMismatchException e){
                in.nextLine(); //consume the user input;
            }
        }

        if(counter == 6) {
            System.out.println("\n\nYOU LOST!!!!");
            System.out.println("The Word was [" + word + "] !!");
        }
        System.out.println("\nWould you like to play again? (y or n)");
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

    public static void hangManTemplate(int counter) {
        switch (counter) {
            case 0 -> System.out.println("________\n|       \n|       \n|               \n|               \n|________");
            case 1 -> System.out.println("________\n|      |\n|      O\n|              \n|            \n|________");
            case 2 -> System.out.println("________\n|      |\n|      O\n|     /        \n|            \n|________");
            case 3 -> System.out.println("________\n|      |\n|      O\n|     /|        \n|            \n|________");
            case 4 -> System.out.println("________\n|      |\n|      O\n|     /|\\        \n|            \n|________");
            case 5 -> System.out.println("________\n|      |\n|      O\n|     /|\\       \n|     /        \n|________");
            case 6 ->
                    System.out.println("________\n|      |\n|      O\n|     /|\\       \n|     / \\       \n|________");
            default -> System.out.println("The guy escaped");
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


    public static String wordRandomizer() {
        List<String> strFiles;
        try {
            strFiles = Files.lines(Paths.get("src/sourceFiles/randomWords.txt")).collect(Collectors.toList());
        }
        catch (IOException e){
            System.err.println("File does not exist");
            return "default";
        }

        Random rand = new Random();
        int randomGenerator = rand.nextInt(strFiles.size() - 1);
        return strFiles.get(randomGenerator);
    }
}
