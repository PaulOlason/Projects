/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Basic visual representation of room. 
 * Makes sure players are correctly
 * positioned and models their current
 * location on the board.
 ********************************************/

package view;

import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import org.w3c.dom.*;
import java.util.*;

import controller.Controller;

public class RoomView extends JLabel {
   protected int[] area;         //Location of room
   protected int players;        //Number of players in room
   protected Controller ctrl;    //Deadwood controller
   protected String name;        //Name of room
   
   public RoomView(Element room, String name, Controller ctrl) {
      super();
      this.name = name;
      this.ctrl = ctrl;
      area = new int[4];
      NodeList dimensionsList = room.getElementsByTagName("area");
      Element dimensions = (Element) dimensionsList.item(0); 
      area[0] = Integer.parseInt(dimensions.getAttribute("x"));       
      area[1] = Integer.parseInt(dimensions.getAttribute("y"));
      area[2] = Integer.parseInt(dimensions.getAttribute("h"));
      area[3] = Integer.parseInt(dimensions.getAttribute("w"));       
      setBounds(area[0],area[1],area[2],area[3]); 
      setVisible(true);
      addMouseListener(
      new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          signalMove();
        }
      });
   }
   
   // Called when room is clicked
   private void signalMove() {
      ctrl.move(name);
   }
   
   // Returns an array representing the players new
   // position in relation to players currently in the room
   public int[] getPlayerLoc() {
      int[] shift = {area[0], area[1]};
      shift[0] = shift[0] + (40 * ((players - 1) % 4));
      if (players > 4) {
         shift[1] = shift[1] + 40;
      }
      return shift;
   }
   
   public int[] getArea() {
      return area;
   }
   
   public void addPlayer() {
      players++;
   }
   
   public void removePlayer() {
      players--;
   }
   
   /* Default methods */
   
   public Marker[] getMarkers() {
      return null;
   }
   
   public void reset() {
      return;
   }
   
   public void removeShot() {
      return;
   }
   
   public int[] getRolePosition(String name) {
      return null;
   }
   
   public Map<String, RoleView> getRoles() {
      return null;
   }
   
   
   public Map<Integer, RankView> getDollars() {
      return null;
   }
   
   public Map<Integer, RankView> getCredits() {
      return null;
   }

}