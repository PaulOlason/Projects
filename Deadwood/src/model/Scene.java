/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Represents the cards in Deadwood.
 * Stores a series of Roles that can be
 * performed by the Players
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;

public class Scene {
   private String name;       //Name of scene
   private int budget;        //Amount needed for a successful roll
   private int sceneNumber;   //Scene number from card
   private String description;//Scene description text
   private Lead[] roles;      //List of roles
   private String image;

   public Scene(Element card) {
      name = card.getAttribute("name");
      image = card.getAttribute("img");
      budget = Integer.parseInt(card.getAttribute("budget"));
      createSceneEls(card.getElementsByTagName("scene"));  
      createRoles(card.getElementsByTagName("part"));
   }
   
   // Gathers scene elements from the given nodelist
   // Uses them to assigen scene number and get description
   private void createSceneEls(NodeList sceneList){
      if ((sceneList.getLength() == 1 &&
          sceneList.item(0).getNodeType() == Node.ELEMENT_NODE)) {
          
         Node sceneNode = sceneList.item(0);
         Element scene = (Element) sceneNode; 
         sceneNumber = Integer.parseInt(scene.getAttribute("number"));
         Node sceneText = sceneNode.getFirstChild();
         if (sceneText.getNodeType() == Node.TEXT_NODE) {
            description = sceneText.getTextContent();
         } 
      } 
   }
   
   // Creates each Role from the given partList and assigns
   // them to the roles array
   private void createRoles(NodeList partList) {
      roles = new Lead[partList.getLength()];
      for (int i = 0; i < partList.getLength(); ++i){
         Node curRole = partList.item(i);
         if (curRole.getNodeType() == Node.ELEMENT_NODE) {
            Lead role = new Lead((Element)curRole, budget);
            roles[i] = role;
         }
      }
   
   }
   
   public int getBudget() {
      return budget;
   }
   
   public Lead[] getRoles() {
      return roles;
   }
   
   public String getName() {
      return name;
   }
   
   public int getNum() {
      return sceneNumber;
   }
   
   // Returns the number of roles with actors
   public int numberLeads() {
      int num = 0;
      for (Role current : roles) {
         if (current.hasPlayer()) {
            num++;
         }
     
      }
      return num;
   }
 
   // Returns a combination of the number and scene name  
   public String name() {
      String fullName = name + " scene: " + sceneNumber;
      return fullName;   
   }
   
   public String getImage() {
      return image;
   }
}