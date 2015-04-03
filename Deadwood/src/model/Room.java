/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * The basic Deadwood Room. Players move
 * between rooms during their turn. Stores a 
 * list of adjacent rooms that may be legally
 * moved to.
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;

public class Room {
   private String[] neighbors;      //List of neighbors
   protected String name;           //Name of room
   

   public Room(Element room) {
      NodeList neighborList = room.getElementsByTagName("neighbors");
      Element NeighborNode = (Element) neighborList.item(0);
      addNeighbors(NeighborNode.getElementsByTagName("neighbor"));
   
   }
   
   // Locates all neighbor names in neighborList
   // Stores them in the neighbors array   
   private void addNeighbors(NodeList neighborList) {
      neighbors = new String[neighborList.getLength()];
      for (int i = 0; i < neighborList.getLength(); i++) {
         Node current = neighborList.item(i);
         if (current.getNodeType() == Node.ELEMENT_NODE) {
            Element room = (Element) current;
            neighbors[i] = room.getAttribute("name");       
         }      
      }
   }
   
   // Returns a list of all neighbors as a string
   public String listNeighbors() {
      String list = neighbors[0];
      for (int i = 1; i < neighbors.length; i++) {
         list = list + ", " + neighbors[i];
      }
      return list;
   }
   
   // Tests if the given String is the name of a neighbor
   // Returns true if found, false otherwise
   public boolean isNeighbor(String neighbor) {
     for (String current : neighbors) {
         if (current.equals(neighbor)) {
            return true;
         }
      }
      return false;
   }
   
   public String getName() {
      return name;
   }
   
   /* Default behaviour for non-Office rooms */
   public boolean rankUp(String type, int rank, Player player) {
      System.out.println("Not in office, cannot rank up.");
      return false;
   }
   
   /* Default behavior for non-set rooms */
   
   public boolean isSet() {
      return false;
   }
   
   public int removeShot() {
      return 0;
   }
   
   public boolean hasScene() {
      return false;
   }
   
   public Role[] getAllRoles() {
      System.out.println("No roles in current room.");
      return null;
   }
   
   public Scene getScene() {
      return null;
   }
   
   public int getTakes() {
      return 0;
   }
   
   public void addScene(Scene card) {
      return;
   }
   
   public int[] getAllPlayers() {
      return null;
   }
   
}