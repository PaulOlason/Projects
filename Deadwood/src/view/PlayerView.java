/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Represents the player in the view. Players
 * store images representing their current
 * rank, and are moved around the board by
 * other classes
 ********************************************/

package view;

import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

import controller.Controller;

public class PlayerView extends JLabel {
   private String color;
   private String location;
   private int[] roomLoc;
   
   public PlayerView(String color) {
      super();
      roomLoc = new int[2];
      this.color = color;
      setImage(1);
      setVisible(true);
   }
   
   // Changes player image to a selected rank   
   public void setImage(int rank) {
      Resources r = Resources.getInstance();
      setIcon(r.getPlayer(color, rank));
   }
   
   public void savePosition(int[] area) {
      roomLoc[0] = area[0];
      roomLoc[1] = area[1];   
   }
   
   public void move(int[] area, String name) {
      location = name;
      setBounds(area[0],area[1],40,40);
   
   }
   
   // Returns the player to their current room
   public void leaveRole() {
      setBounds(roomLoc[0],roomLoc[1],40,40);  
   }
   
   public String getLoc() {
      return location;
   }



}