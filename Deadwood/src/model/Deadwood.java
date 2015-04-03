/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * This program allows the user to play
 * the game Deadwood. It takes one 
 * variable as input, the integer number
 * of players which must be greater than
 * one. It accepts the commands "who", 
 * "where", "move [room]", upgrade [$ or cr]
 * [level]", "reherse", "act" and "end" to
 * end the current players turn.
 ********************************************/

package model;

import java.util.*;
import java.io.*;

import controller.Controller;

public class Deadwood {
   //SHUFFLE indicates if deck will be randomized
   //Set to fales to use card order in XML file
   private static final boolean SHUFFLE = true;
   private static Scanner input;         //Takes user input
   private static Board board;           //Stores player info
   private static int actions;

   // Runs a game of Deadwood for args[0] number of players
   // arg[0] must be an integer, and must be between 2 and 8
   // Exits if any other arguments are passed
   public static void main(String[] args) {
      try {
         if ((Integer.parseInt(args[0]) <= 1) || (Integer.parseInt(args[0]) > 8)) {
            System.out.println("Invalid player number.");            
         } else {
            System.out.println("Welcome to Deadwood.");
            System.out.println("Loading...");
            int noPlayers = Integer.parseInt(args[0]);
            board = new Board(noPlayers, SHUFFLE);
            input = new Scanner(System.in);
            System.out.println("Currently: " + noPlayers + " players playing.");
            namePlayers(noPlayers);
            board.activateFrame();            
         }         
      } catch (NumberFormatException e) {
         System.out.println("Invalid argument. Must be integer");         
      }     
   }
   
   // Sets player's usernames to match the defualt names
   // Each one corresponds with a dice color 
   private static void namePlayers(int noPlayers) {
      String[] names = {"Blue", "Red", "Yellow", "Green", 
            "Cyan", "Pink", "Orange", "Violet"};
      System.out.println("Players are: ");
      for (int i = 0; i < noPlayers; i++) {
         Player player = board.currentPlayerNoChange();
         System.out.print("Player " + (i + 1));
         System.out.println(": " + names[i]);
         player.setName(names[i]);    
         board.changePlayer();   
      }      
   }
   
}