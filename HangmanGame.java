package a01;

import java.util.Random;
import java.util.Scanner;

/**
 * Generates a hangman game for someone to play in the console.
 * Has a limited work bank to choose from, and the lives are fixed.
 * @author malcolm bailey
 *
 */
public class HangmanGame {
	
	
	private int totalLives = 6;
	private String secretWord;
	public String revealedWord;
	private char[] previousGuesses = new char[26];
	private int currentLives;
	private String[] secretWordBank = {
			"Panoply", "Utah", "Mississippi", "Mouth", "Skeleton",
			"Water", "Guitar", "telephone", "Beer", "Computer", "Software"
	};
	
	/**
	 * Constructor generates and runs the game, first randomly selecting a word from the word bank.
	 * Then allowing the player to guess as long as they have remaining lives and the word isn't solved.
	 */
	public HangmanGame() {
		
		// Select a random word from our word bank constructed above.
		Random r =  new Random();
		this.currentLives = totalLives;
		secretWord = secretWordBank[r.nextInt(secretWordBank.length)].toLowerCase();
		revealedWord = "_".repeat(secretWord.length());
		
		// Check to see if the game is over, if not, ask the player to guess.
		while (!gameWon() && !gameLost()) {
			
			guess();
		}
		
		// If the game is over, display the game over screen.
		gameOverScreen();
	} 
	
	/**
	 * The main loop for the game, this will ask for a letter guess, and check to see
	 * whether that letter has been guessed before, and whether it's formatted correctly.
	 * Assuming both are true, it will submit the guess.
	 */
	private void guess() {
		
		// Show the player their current progress.
		showProgress();
		
		// Query for a guess using a scanner object.
		System.out.println("Enter a single letter guess: ");
		Scanner s = new Scanner(System.in);
		String guess = s.next().strip().toLowerCase();
		
		// Verify that the guess is one letter that hasn't been previously guessed.
		if (guess.length() != 1 || !isValidGuess(guess)) {
			
			System.out.println("The guess needs to be a single letter that "
					+ "you haven't already guessed!");
			guess();
			
		} else {
			
			// The guess is valid, so we submit it to the game.
			char guessChar = guess.charAt(0);
			submitLetter(guessChar);
		}
	}
	
	/**
	 * Verifies that the guess is both a letter and that it hasn't been guessed before.
	 * 
	 * @param guessString : the passed in guess from our main guess() method.
	 * @return
	 */
	private boolean isValidGuess(String guessString) {
		
		// We know that the guess is a single character at this point, but we need to check if it's
		// a letter.
		char guessChar = guessString.charAt(0);
		if (!Character.isLetter(guessChar)) return false;
		
		for (int i = 0; i < 26; i++) {
			if(this.previousGuesses[i] == guessChar) {
				
				return false;
			} else if (this.previousGuesses[i] == 0) {
				
				break;
			}
		}
		return true;	
	}
	
	/**
	 * Submits a valid guess, which will then check if it's contained within the word, then 
	 * display it if it is. Otherwise, it will just subtract a life. Both ways, the letter will then be added
	 * to the list of previous guesses.
	 * 
	 * @param guess : The single letter guess that has now been validated.
	 */
	private void submitLetter(char guess) {
		
		/*
		 *  Create two substrings in order to replace a character at a specific index
		 *  which allows us to reveal the world character by character.
		 */
		String substr1;
		String substr2;
		
		// Add the guess to our previous guesses.
		for (int i = 0; i < 26; i++) {
			if(this.previousGuesses[i] == 0) {
				this.previousGuesses[i] = guess;
				break;
			}
		}
		
		// Assume the guess is wrong, then check to see if we have any matches in the secret word.
		boolean correctGuess = false;
		
		for (int i = 0; i < this.secretWord.length(); i ++) {
			if (this.secretWord.charAt(i) == guess) {
				
				/*
				 *  Since we have found an instance of the letter, split the revealed word into
				 *  two substrings, and replace the '_' character at the appropriate index
				 *   with the same letter that was correct.
				 */
				substr1 = this.revealedWord.substring(0, i);
				substr2 = guess + this.revealedWord.substring(i + 1);
				this.revealedWord = substr1 + substr2;
				correctGuess = true;
			}
		}
		
		// Assuming we couldn't find any matches, the guess is wrong and the player loses a life.
		if(!correctGuess) {
			loseLife();
		}
	}
	
	/**
	 * Shows the player their historical guesses.
	 * 
	 * @return a string of all their previous guesses.
	 */
	public String showGuesses() {
		
		StringBuilder guesses = new StringBuilder("Previous letters guessed are: ");
		char letter;
		
		// create a list of characters we have already guessed, then return it.
		for (int i = 0; i < 26; i++) {
			
			letter = this.previousGuesses[i];
			
			if (letter != 0 && i > 0) {
				guesses.append(", " + letter);
			} else if(letter != 0 && i == 0){
				guesses.append(" " + letter);
			} else {
				guesses.append(String.format("\n"));
				break;
			}
		}
		
		return guesses.toString();	
	}
	
	/**
	 * Getter for the life count
	 * @return current number of lives remaining
	 */
	public int getLives() {
		return this.currentLives;
	}
	
	/**
	 * Removes a single life from the players current life count.
	 */
	private void loseLife() {
		this.currentLives --;
	}
	
	/**
	 * Checks to see whether all letters have been revealed.
	 * 
	 * @return true if the word is complete, otherwise false
	 */
	private boolean gameWon() {
		if(revealedWord.equals(secretWord)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Displays the players progress in this game, including the so-far revealed word, and their remaining lives.
	 */
	private void showProgress() {
		
		StringBuilder displayRevealedWord = new StringBuilder();
		
		// adds the revealed word to a string, with spaces in between for formatting.
        for (int i = 0; i < revealedWord.length(); i ++) {
            displayRevealedWord.append(this.revealedWord.charAt(i) + " ");
        }
        
        // add the newly-formatted revealed word to a string that also shows the previous guesses.
		StringBuilder progressString = new StringBuilder();
		progressString.append(String.format("\n") + "Current Word: "
				+ displayRevealedWord.toString() + String.format("\n"));
		
		progressString.append(showGuesses());
		
		// add the players lives to the string, then print it.
		progressString.append("You have " + getLives() + " lives remaining.");
		System.out.println(progressString.toString());
	}
	
	/**
	 * Checks to see if the player has lost (i.e. has no more lives)
	 * @return true if they have 0 lives, otherwise false.
	 */
	private boolean gameLost() {
		return this.currentLives == 0;
	}
	
	/**
	 * Prints the end-game screen, depending on whether the player has won or lost.
	 * Then queries the player once to see if they want to play again.
	 */
	private void gameOverScreen() {
		
		StringBuilder gameOverString = new StringBuilder();
		
		/*
		 * 	since this method is only called when the game is won or lost, check if it's lost then assume it's won,
		 *  then build the appropriate screen including the secret word and print their result.
		 */
		
		if(gameLost()) {
			gameOverString.append("Sorry, you lose! The word was: " + this.secretWord);
		} else {
			gameOverString.append("You win! The word was: " + this.secretWord);
		}
		
		System.out.println(gameOverString.toString());
		
		System.out.println("Type \"y\" to play again ");
		Scanner s = new Scanner(System.in);
		String input = s.next().strip().toLowerCase();
				
		if (input.equals("y")) {
			
			HangmanGame hg = new HangmanGame();
		}
		
	}

	public static void main(String[] args) {
		HangmanGame hg = new HangmanGame();
	}

}
