/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Subclass of Room. Sets are Rooms that may
 * or may not contain a Scene. Scenes can
 * be removed and replaced with other Scenes.
 * Sets also contain a set of Extra roles
 * that may be performed by the player
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;

public class Set extends Room {
   private int defaultTakes;
   private int takes;
   private Extra[] roles;
   private Scene scene;

   public Set(Element room) {
      super(room);
      takes = 0;
      name = room.getAttribute("name");
      NodeList takeList = room.getElementsByTagName("takes");
      if ((takeList.getLength() == 1 &&
          takeList.item(0).getNodeType() == Node.ELEMENT_NODE)) {
          Element takeNode = (Element) takeList.item(0);
          NodeList takes = takeNode.getElementsByTagName("take");
          defaultTakes = takes.getLength();
      }
      NodeList partList = room.getElementsByTagName("parts");
      if ((partList.getLength() == 1 &&
          partList.item(0).getNodeType() == Node.ELEMENT_NODE)) {
          
         Element partNode = (Element) partList.item(0);
         createRoles(partNode.getElementsByTagName("part"));
      }
   }
   
   // Constructs all Roles using information in partList
   // Stores each Role in the roels array
   private void createRoles(NodeList partList) {
      roles = new Extra[partList.getLength()];
      for (int i = 0; i < partList.getLength(); ++i){
         Node curRole = partList.item(i);
         if (curRole.getNodeType() == Node.ELEMENT_NODE) {
            Extra role = new Extra((Element)curRole, 7);
            roles[i] = role;
         }
      }
   
   }
   
   // Assigns a Scene to the Set
   public void addScene(Scene card) {
      takes = defaultTakes;
      scene = card;
      int budget = scene.getBudget();
      for (Extra current : roles) {
         current.setBudget(budget);
      
      }
   }
   
   // Removes the Scene and resets the budget of all roles
   public void removeScene() {
      scene = null;
      for (Extra current : roles) {
         current.setBudget(7);      
      }  
   }
   
   // Builds a combined array of all Extra and Lead roles  
   public Role[] getAllRoles() {
      Role[] leads = scene.getRoles();
      Role[] combined = new Role[leads.length + roles.length];
      int index = 0;
      for (Role current : leads) {
         combined[index] = current;
         index++;
      }
      for (Role current : roles) {
         combined[index] = current;
         index++;
      }
      return combined;
   }
   
   public int[] getAllPlayers() {
      Role[] allRoles = getAllRoles();
      int[] players = new int[allRoles.length];
      for (int i = 0; i < allRoles.length; i++) {
         if (allRoles[i].hasPlayer()) {
            players[i] = allRoles[i].getPlayer().getId();
         } else {
            players[i] = -1;
         }
      }
      return players;
   }
   
   // Signals that a shot has succeeded, gives rewards when
   // all have been removed
   public int removeShot() {
      takes--;
      if (takes == 0) {
         giveRewards(hasLeads());
         return 1;
      }
      return 0;
   }
   
   // Returns true if any roles in scene have actors
   public boolean hasLeads() {
      if (scene != null) {
         int num = scene.numberLeads();
         if (num > 0) {
            return true;
         }
      }
      return false;
   }
   
   // If reward is true, distributes rewards to each Player
   // Removes scene and resets each Player's Role
   private void giveRewards(boolean reward) {
      System.out.print("Scene completed.");
      if (reward) {
         System.out.println(" Distributing rewards.");
         leadReward(); 
      } else {
         System.out.println();
      }
      for (Extra current : roles) {
         if (current.hasPlayer()) {
            current.rewardExtra(reward);
         }
      }
      removeScene();
   }
   
   // Randomly generates values from 1 - 6 to be rewarded
   // to each Lead role
   private void leadReward() {
      int dice = scene.getBudget();
      List<Integer> money = new ArrayList<Integer>();
      for (int i = 0; i < dice; i ++) {
         money.add(roles[0].roll());
      }
      Collections.sort(money);
      payActive(money);
      
      
   }
   
   // Gathers and orders active Leads according to rank
   // Splits values in money between them
   private void payActive(List<Integer> money) {
      int index = 0;
      Lead[] leads = scene.getRoles();
      Lead[] active = new Lead[scene.numberLeads()];
      for (Lead current: leads) {
         if (current.hasPlayer()) {
            active[index] = current;
            index++;
         }
      }
      index = 0;
      for (int current : money) {
         active[index].rewardLead(current);
         index++;
         if (index == active.length) {
            index = 0;
         }
      }
   }
   
   public boolean isSet() {
      return true;
   }
   
   public boolean hasScene() {
      return (scene != null);
   }
   
   public Scene getScene() {
      return scene;
   }
   
   public int getTakes() {
      return takes;
   }
   
   public Role[] getRoles() {
      return roles;
   }
   
}