package a01;

import java.util.Random;
import java.util.Scanner;

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
	
	public HangmanGame() {
		
		Random r =  new Random();
		this.currentLives = totalLives;
		secretWord = secretWordBank[r.nextInt(secretWordBank.length)].toLowerCase();
		revealedWord = "_".repeat(secretWord.length());
		
		while (!gameWon() && !gameLost()) {
			
			guess();
		}
		
		gameOverScreen();
	} 
	
	private void guess() {
		
		showProgress();
		System.out.println("Enter a single letter guess: ");
		Scanner s = new Scanner(System.in);
		String guess = s.next().strip().toLowerCase();
		
		
		if (guess.length() != 1 || !isValidGuess(guess)) {
			
			System.out.println("The guess needs to be a single letter that "
					+ "you haven't already guessed!");
			guess();
			
		} else {
			
			char guessChar = guess.charAt(0);
			submitLetter(guessChar);
		}
	}
	
	private boolean isValidGuess(String guessString) {
		
		
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
	
	private void submitLetter(char guess) {
		String substr1;
		String substr2;
		
		for (int i = 0; i < 26; i++) {
			if(this.previousGuesses[i] == 0) {
				this.previousGuesses[i] = guess;
				break;
			}
		}
		
		boolean correctGuess = false;
		
		for (int i = 0; i < this.secretWord.length(); i ++) {
			if (this.secretWord.charAt(i) == guess) {
				substr1 = this.revealedWord.substring(0, i);
				substr2 = guess + this.revealedWord.substring(i + 1);
				this.revealedWord = substr1 + substr2;
				correctGuess = true;
			}
		}
		
		if(!correctGuess) {
			loseLife();
		}
	}
	
	public String showGuesses() {
		
		StringBuilder guesses = new StringBuilder("Previous letters guessed are: ");
		char letter;
		
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
	
	public int getLives() {
		return this.currentLives;
	}
	
	private void loseLife() {
		this.currentLives --;
	}
	
	private boolean gameWon() {
		if(revealedWord.equals(secretWord)) {
			return true;
		}
		return false;
	}
	
	private void showProgress() {
		StringBuilder displayRevealedWord = new StringBuilder();
		
        System.out.flush();
        for (int i = 0; i < revealedWord.length(); i ++) {
            displayRevealedWord.append(this.revealedWord.charAt(i) + " ");
        }

		StringBuilder progressString = new StringBuilder();
		progressString.append(String.format("\n") + "Current Word: "
				+ displayRevealedWord.toString() + String.format("\n"));
		
		progressString.append(showGuesses());
		
		progressString.append("You have " + getLives() + " lives remaining.");
		System.out.println(progressString.toString());
	}
	
	private boolean gameLost() {
		return this.currentLives == 0;
	}
	
	private void gameOverScreen() {
		StringBuilder gameOverString = new StringBuilder();
		
		if(gameLost()) {
			gameOverString.append("Sorry, you lose! The word was: " + this.secretWord);
		} else {
			gameOverString.append("You win! The word was: " + this.secretWord);
		}
		
		System.out.println(gameOverString.toString());
	}

	public static void main(String[] args) {
		HangmanGame hg = new HangmanGame();
	}

}
