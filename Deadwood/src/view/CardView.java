/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Represent scenes in the view. Relocates
 * player when selected.
 ********************************************/

package view;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.image.*;
import org.w3c.dom.*;

import controller.Controller;

public class CardView extends JLabel {
   private JLabel board;
   private Map<String, RoleView> roles;
   private ImageIcon card;
   private int[] cardLoc;
   private String name;
   private String room;
   private Controller ctrl;
   private String image;
   
   
   public CardView(Element scene, Controller ctrl) {
      cardLoc = new int[4]; 
      cardLoc[2] = 205;
      cardLoc[3] = 115; 
      name = scene.getAttribute("name");
      image = scene.getAttribute("img");
      setImage(image);
      this.ctrl = ctrl;
      roles = new HashMap<String, RoleView>();
      NodeList partList = scene.getElementsByTagName("part");
      scenePositions(partList, ctrl);
      
      addMouseListener(
      new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          signalMove();
        }
      });
      setVisible(true);
   } 
   
   //Called when card is clicked
   private void signalMove() {
      ctrl.move(room);
   }
   
   public void setRoom(String room) {
      this.room = room;
   }
   
   public void setImage(String image) {
      Resources r = Resources.getInstance();
      setIcon(r.getCard("cards/" + image));
   }
   
   //Pulls role information from the given node
   private void scenePositions(NodeList partList, Controller ctrl) {
      for (int i = 0; i < partList.getLength(); ++i){
         Node curRole = partList.item(i);
         if (curRole.getNodeType() == Node.ELEMENT_NODE) {
            Element role = (Element) curRole;
            String roleName = role.getAttribute("name").toLowerCase();
            NodeList dimensionList = role.getElementsByTagName("area");
            Element dimensions = (Element) dimensionList.item(0);
            int[] area = new int[4];
            area[0] = Integer.parseInt(dimensions.getAttribute("x"));
            area[1] = Integer.parseInt(dimensions.getAttribute("y"));
            area[2] = Integer.parseInt(dimensions.getAttribute("h"));
            area[3] = Integer.parseInt(dimensions.getAttribute("w"));
            RoleView roleView = new RoleView(roleName, ctrl, area);
            roles.put(roleName, roleView);
         }
      }
   }
   
   //Places the card on the board
   public void place(int[] area) {
      cardLoc = new int[4];
      cardLoc[0] = area[0];
      cardLoc[1] = area[1];
      setBounds(area[0],area[1],205,115);
      for (String current: roles.keySet()) {
         RoleView roleView = roles.get(current);
         roleView.setPosition(area);
      }
      hideCard();
   }
   
   // Locatse positions of roles in relation
   // to the card's position
   public int[] getRolePosition(String name) {
      RoleView role = roles.get(name);
      int[] mod = role.getMod();
      int[] pos = new int[2];
      pos[0] = cardLoc[0] + mod[0];
      pos[1] = cardLoc[1] + mod[1];
      return pos;
   }
   
   public String getName() {
      return name;
   }
   
   public String getImage() {
      return image;
   }
     
         
   public void showCard() {
      setVisible(true);
   }
   
      
   public void hideCard() {
      setVisible(false);
   }
   
   public Map<String, RoleView> getRoles(){
      return roles;
   }
   
}