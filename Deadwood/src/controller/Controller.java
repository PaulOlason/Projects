/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Delivers input from user to the model.
 * Calls correct methods when day ends and 
 * when game terminates
 ********************************************/

package controller;

import java.util.*;
import java.io.*;

import model.Board;

public class Controller {
   private Board board;       //Deadwood board, used to execute commands
   private boolean endOfDay;  //True once all days have occured
   
   public Controller(Board board) {
      this.board = board;
   }
   
   public void move(String room) {
      board.moveClick(room);
   }
   
   public void takeRole(String role) {
      board.workClick(role);
   }
   
   public void act() {
      int result = board.actClick();
      if (result == 4) {
         endOfDay = (board.sceneEnd() == 1);
      }
   }
   
   public void rehearse() {
      board.rehearseClick();
   }
   
   public void endTurn() {
      if ((endOfDay) && (board.endOfDay())) {
         board.results();
      } else {
         board.endTurnClick();
         endOfDay = false;
      }
   }
   
   public void upgrade(int rank, String currency) {
      board.upgradeClick(currency, rank);
   }

}