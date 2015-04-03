/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Contains reference to all scenes, and
 * distributes unused scenes to other methods.
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;

public class Deck {
   private Scene[] cards;     //Stores all cards
   private int index;         //Index of current card

   public Deck() {
      cards = new Scene[40];
      index = 0; 
   }
   
   // Creates a Scene using the given XML element
   // Adds the Scene to the cards array 
   public void addCard(Element card) {
      Scene newCard = new Scene(card);
      cards[index] = newCard;
      index++;
   
   }
   
   // Randomly sorts all cards
   public void shuffle() {
      Random rand = new Random();
      for (int i = cards.length - 1; i > 0; i--) {
         int move = rand.nextInt(i);
         Scene store = cards[move];
         cards[move] = cards[i];
         cards[i] = store;
      }
   }

   public Scene getCard() {
      index--;
      return cards[index];
   }


}