/********************************************
 *        Paul Olason - Assignment 2
 *          CSCI 412 - Spring 2015
 *
 *  Plays a game of hangman with the user.
 *  The user attempts to guess a hidden
 *  word before the computer has drawn a full
 *  hangman. Custom words can be entered using
 *  the "Custom Game" option.
 ********************************************/

package com.week3.csci.hangman_game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HangMan extends ActionBarActivity {
    private List<Character> guessed;    // List of guessed characters
    private List<Character> found;      // List of correctly guessed characters
    private List<Character> key;        // List of unique letters in word
    private String word;                // Current word being guessed
    private String prompt;              // Display for player prompt
    private String guessed_chars;       // Display for guessed characters
    private int limbs;                  // Number of remaining incorrect guesses
    private int guesses;                // Number of letters guessed so far

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hang_man);
        guessed = new ArrayList<>();
        found = new ArrayList<>();
        key = new ArrayList<>();
        newGame(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hang_man, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new_game) {
            newGame(null);
        } else if (item.getItemId() == R.id.action_custom) {
            customGame();
        }
        return super.onOptionsItemSelected(item);
    }

    // Takes player input and attempts to make
    private void customGame() {
        AlertDialog menu = new AlertDialog.Builder(this).create();
        menu.setMessage("Enter a new word to use");
        final EditText input = new EditText(this);
        menu.setView(input);
        menu.getListView();
        menu.setButton (AlertDialog.BUTTON_POSITIVE, "Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    newGame(input.getText().toString().toLowerCase());
            }
        });
        menu.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancels the option selection
            }
        });

        menu.show();

    }


    // Resets all values, chooses a new word, and starts a new game
    // If userWord is set to null, word is randomly generated
    // Otherwise, word will be set to userWord if it is a valid word
    public void newGame(String userWord) {
        boolean valid = true;
        if (userWord == null) {
            newWord();
        } else {
            for (int i = 0; i < userWord.length(); i++) {
                // checks if userWord contains invalid characters
                if (!Character.isLetter(userWord.charAt(i))) {
                    valid = false;
                    break;
                }
            }
        }
        if (valid) {
            guessed.clear();
            found.clear();
            key.clear();
            prompt = "Guess a letter!\n(6 incorrect guesses left)";
            guessed_chars = "Guessed Values: ";
            limbs = 6;
            guesses = 0;
            if (userWord == null) {
                newWord();
            } else {
                word = userWord;
            }
            getLetters();
        } else {
            prompt = "Please enter a valid word\n";
            guessesLeft();
        }
        updateView();
    }

    // Randomly selects a new word from the dictionary
    private void newWord() {
        String[] wordList = getResources().getStringArray(R.array.words);
        Random rand = new Random();
        int index = rand.nextInt(5442);
        word = wordList[index];
    }

    // Stores each unique letter from word into key
    private void getLetters() {
        for (int i = 0; i < word.length(); i++) {
            char current = word.charAt(i);
            if (!key.contains(current)) {
                key.add(current);
            }
        }
    }

    // Pulls the player input and tests if they guessed correctly
    public void guess_click(View view) {
        EditText input = (EditText) findViewById(R.id.editText);
        String guess = input.getText().toString().toLowerCase();
        input.setText("");
        checkGuess(guess);
    }

    // Checks the format of guess, and investigates if it is correct
    // Modifies the prompt to indicate if a guess was correct or incorrect
    public void checkGuess(String guess) {
        prompt = "You guessed : " + guess + "\n";
        if (guess.length() == 1) {
            checkLetter(guess.charAt(0));
        } else if (guess.length() == 0) {
            prompt = "Please enter a letter\n";
        } else {
            guesses++;
            if (word.equals(guess)) {
                // The player correctly guesses the word
                endGame(true);
            } else {
                incorrect();
            }
        }
        guessesLeft();
        updateView();
    }

    // Sets the prompt to display the number of remainging guesses
    public void guessesLeft() {
        if (limbs > 1) {
            prompt = prompt + "\n(" + limbs + " incorrect guesses left)";
        } else if (limbs == 1) {
            prompt = prompt + "\n(" + limbs + " incorrect guess left)";
        } else {
            // The player has run out of incorrect guesses
            endGame(false);
        }
    }

    // Checks if guess is contained in the final word
    // Modifies the prompt if guess is not a letter, or has already been guessed
    private void checkLetter(char guess) {
        if (guessed.contains(guess)) {
            prompt = prompt + "Already guessed letter ";
        }else if (!Character.isLetter(guess)) {
            prompt = prompt + "Please enter a letter";
        } else {
            guesses++;
            if (key.contains(guess)) {
                found.add(guess);
                prompt = prompt + "Correct! ";
            } else {
                incorrect();
            }
            guessed.add(guess);
            guessed_chars = guessed_chars + " " + guess;
        }
    }

    // Ends the game and asks the player if they would like to play again
    // The player has won when win == true, and lost if not
    private void endGame(boolean win) {
        AlertDialog endgame_menu = new AlertDialog.Builder(this).create();
        String msg;
        if (win) {
            msg = "You found " + word + " in " + guesses + " guesses!\n";
        } else {
            msg = "You lose! word was " + word + ".\n";
        }
        msg = msg + "Play again?";
        endgame_menu.setMessage(msg);
        endgame_menu.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newGame(null);
            }
        });
        endgame_menu.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });

        endgame_menu.show();
    }

    // Detriments the number of remaining incorrect guesses
    // Modifies message to indicate the player was wrong
    private void incorrect() {
        limbs--;
        prompt = prompt + "Incorrect Guess ";
    }

    // Updates the display to show all current stats
    private void updateView() {
        TextView disp = (TextView) findViewById(R.id.prompt);
        disp.setText(prompt);
        disp = (TextView) findViewById(R.id.prev_guess);
        disp.setText(guessed_chars);
        dispWord();
        updatePicture();
    }

    // Modifies the hangman picture biased on the number of incorrect guesses remaining
    private void updatePicture() {
        ImageView hang = (ImageView) findViewById(R.id.hangman);
        switch (limbs) {
            case 6:
                hang.setImageResource(R.drawable.hangman6);
                return;
            case 5:
                hang.setImageResource(R.drawable.hangman5);
                return;
            case 4:
                hang.setImageResource(R.drawable.hangman4);
                return;
            case 3:
                hang.setImageResource(R.drawable.hangman3);
                return;
            case 2:
                hang.setImageResource(R.drawable.hangman2);
                return;
            case 1:
                hang.setImageResource(R.drawable.hangman1);
                return;
            case 0:
                hang.setImageResource(R.drawable.hangman0);
        }
    }

    // Displays the word with "?" in place of letters not yet found
    private void dispWord() {
        TextView word_text = (TextView) findViewById(R.id.word);
        String current_word = "";
        for (int i = 0; i < word.length(); i++) {
            if (found.contains(word.charAt(i))) {
                current_word = current_word + word.charAt(i);
            } else {
                current_word = current_word + "?";
            }
        }
        word_text.setText(current_word);
    }
}
