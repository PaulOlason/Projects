/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Subclass of room used to represent the
 * office. The office contains RankViews which
 * can be selected by the player to increase
 * their rank.
 ********************************************/

package view;

import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;
import org.w3c.dom.*;

import controller.Controller;

public class OfficeView extends RoomView {
   private Map<Integer, RankView> dollars;
   private Map<Integer, RankView> credits;

   public OfficeView(Element room, String name, Controller ctrl) {
      super(room, name, ctrl);
      dollars = new HashMap<Integer, RankView>();
      credits = new HashMap<Integer, RankView>();
      NodeList upgradeList = room.getElementsByTagName("upgrades");
      Element upgradeNode = (Element) upgradeList.item(0);
      findRanks(upgradeNode.getElementsByTagName("upgrade"), ctrl);
   }
   
   //Finds all rank information in the given node
   private void findRanks(NodeList rankList, Controller ctrl) {
      for (int i = 0; i < rankList.getLength(); i++) {
         Node current = rankList.item(i);
         if (current.getNodeType() == Node.ELEMENT_NODE) {
            Element room = (Element) current;
            int level = Integer.parseInt(room.getAttribute("level"));
            RankView rank;  
            if (room.getAttribute("currency").equals("dollar")) {
               rank = new RankView(level, "$", ctrl);
               dollars.put(level, rank);
            } else {
               rank = new RankView(level, "cr", ctrl);
               credits.put(level, rank);
            } 
            int[] loc = new int[4];
            NodeList dimensionsList = room.getElementsByTagName("area");
            Element dimensions = (Element) dimensionsList.item(0); 
            loc[0] = Integer.parseInt(dimensions.getAttribute("x"));       
            loc[1] = Integer.parseInt(dimensions.getAttribute("y"));
            loc[2] = Integer.parseInt(dimensions.getAttribute("h"));
            loc[3] = Integer.parseInt(dimensions.getAttribute("w")); 
            rank.setLocation(loc);
            
         }      
      }
   
   }
   /********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Represents the player in the view
 ********************************************/
   public int[] getPlayerLoc() {
      int[] shift = {area[0] + 40, area[1]};
      shift[0] = shift[0] + (40 * (players - 1));
      return shift;
   }
   
   public Map<Integer, RankView> getDollars() {
      return dollars;
   }
   
   public Map<Integer, RankView> getCredits() {
      return credits;
   }
   



}