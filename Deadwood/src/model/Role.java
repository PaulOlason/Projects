/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Abstract form of a roles associated with
 * Scenes and Sets. May or may not have an
 * associated Player. Performs dice roles
 * and calculates when performances are
 * successful or not.
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;

public abstract class Role {
   protected String name;   //Name of Role
   protected int rank;      //Minimum rank to perform
   protected String line;   //Character's dialogue
   protected Player actor;  //Player performing role
   protected int rehearsals;//Bonus added to rolls
   protected int budget;    //Amount needed for rolls to succeed
   protected Random dice;   //Used to generate rolls
   
   public abstract void pay(boolean act);

   public Role(Element role, int budget){
      name = role.getAttribute("name");
      rank = Integer.parseInt(role.getAttribute("level"));
      getLine(role.getElementsByTagName("line"));
      actor = null;
      rehearsals = 0;
      this.budget = budget;
      dice = new Random();
   }
   
   // Searches lineList for the Role's line
   private void getLine(NodeList lineList) {
      if (singleElement(lineList)) {
         Node lineNode = lineList.item(0);    
         Node lineText = lineNode.getFirstChild();
         if (lineText.getNodeType() == Node.TEXT_NODE) {
            line = lineText.getTextContent();
         }       
      }
   }
   
   // Tests if the nodeList contains a single element
   private boolean singleElement(NodeList list) {
      return (list.getLength()==1 &&
          list.item(0).getNodeType() == Node.ELEMENT_NODE);   
   }
   
   // Adds to rehearsals if rolls not already garenteed
   // Returns true of rehearsals is modified
   public boolean rehearse() {
      if (rehearsals < budget) {
         rehearsals++;
         return true;
      } else {
         return false;
      }
   }
   
   // Returns true if a roll succeeds
   public boolean act() {
      int result = roll() + rehearsals;
      return (budget <= result);
   }
   
   // Generates a new random dice value
   public int roll() {
      return dice.nextInt(6) + 1;
   }
   
   // Default behaviour for role
   
   public void dropRole() {
      actor.setRole(null);
      rehearsals = 0;
   }
   
   public boolean isLead() {
      return false;
   }
   
   public Player getPlayer() {
      return actor;
   }
   
   public boolean hasPlayer() {
      return (actor != null); 
   }
   
   public boolean canPlay(int playerRank) {
      return (rank <= playerRank);
   }
   
   public void setPlayer(Player actor) {
      this.actor = actor;
   }
   
   
   public int getRehearsals() {
      return rehearsals;
   }
   
   public String getLine() {
      return line;
   }
   
   public int getRank() {
      return rank;
   }
   
   public String getName() {
      return name;
   }
   
}