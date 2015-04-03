/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Subclass of Room. The Office may be used
 * to increase a Players rank by spending
 * money or credit. Only one Office is
 * created per instance of Board.
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;


public class Office extends Room {
   private Map<Integer, Integer[]> ranks; //Maps all ranks to their price
                                          //in money and credit respectively


   public Office(Element room) { 
      super(room);
      name = "office";
      NodeList rankList = room.getElementsByTagName("upgrades");
      if ((rankList.getLength() == 1 &&
          rankList.item(0).getNodeType() == Node.ELEMENT_NODE)) {
          
         Element rankNode = (Element) rankList.item(0);
         createRanks((rankNode).getElementsByTagName("upgrade"));
      }
     
   }
   
   // Extracts rank and pricing information from rankList
   // Maps each rank to its cost in both money and credit
   private void createRanks(NodeList rankList) {
      ranks = new HashMap<Integer, Integer[]>();
      for (int i = 0; i < rankList.getLength(); i++) {
         Node current = rankList.item(i);
         if (current.getNodeType() == Node.ELEMENT_NODE) {
            Element room = (Element) current;
            Integer level = Integer.parseInt(room.getAttribute("level"));
            Integer[] prices;
            if (ranks.containsKey(level)) {
               prices = ranks.get(level);
               ranks.remove(level);   
            } else {
               prices = new Integer[2];
            }
            if (room.getAttribute("currency").equals("dollar")) {
               prices[0] = Integer.parseInt(room.getAttribute("amt"));
            } else {
               prices[1] = Integer.parseInt(room.getAttribute("amt"));
            }
            ranks.put(level, prices);
         }      
      }
   
   }
   
   // Attempts to upgrade player to rank using the currency
   // indicated by type. Returns true if successful
   public boolean rankUp(String type, int rank, Player player) {
      if ((rank <= 1) || (rank > 6)) {
         System.out.println("Invalid rank.");
         return false;
      }
      Integer[] price = ranks.get(rank);
      if ((type.equals("$")) && (upgrade("money", rank, player))) {
         return true;
      } else if ((type.equals("cr")) && (upgrade("credit", rank, player))) {
         return true;
      } else if ((!type.equals("cr")) && (!type.equals("$"))) {
         System.out.println("Must upgrade with \"$\" or \"cr\"");
      }
      return false;
   }
   
   // Modifies player's rank by spending type
   // Returns true when rank is changed, false otherwise
   public boolean upgrade(String type, int rank, Player player) {
      Integer[] price = ranks.get(rank);
      int cost = 0;
      boolean upgrade;
      if (type.equals("money")) {
         cost = price[0] * -1;
         upgrade = (player.changeMoney(cost));
      } else {
         cost = price[1] * -1;
         upgrade = (player.changeCredit(cost));
      }
      if (upgrade) {
         player.setRank(rank);
         System.out.println("Rank changed to " + rank + ".");
         return true;
      }
      System.out.print("Not enough " + type + ".");
      System.out.println(" Cannot afford rank.");
      return false;
   }
}