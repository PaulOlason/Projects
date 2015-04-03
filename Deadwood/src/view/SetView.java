/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Subclass of RoomView, used to model sets
 * in the view. Contains Markers counters and
 * several RoleViews, and modifies their 
 * status to reflect changes in the model
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

public class SetView extends RoomView {
   private Marker[] marker;                  //Array of all Markers
   private int[][] shots;                    //Locations of each Marker
   private int length;                       //Number of Markers
   private int used;                         //Number of used markers
   private Map<String, int[]> roles;         //Location of each role
   private Map<String, RoleView> clickRoles; //JLabels representing roles
   
   public SetView(Element room, String name, Controller ctrl) {
      super(room, name, ctrl);
      NodeList takeList = room.getElementsByTagName("takes");
      if ((takeList.getLength() == 1 &&
          takeList.item(0).getNodeType() == Node.ELEMENT_NODE)) {
         Element takeNode = (Element) takeList.item(0);
         NodeList takes = takeNode.getElementsByTagName("take");
         length = takes.getLength();
         shots = new int[length][4];
         findTakes(takes);
         NodeList partList = room.getElementsByTagName("parts");
         roles = new HashMap<String, int[]>();
         if ((partList.getLength() == 1 &&
               partList.item(0).getNodeType() == Node.ELEMENT_NODE)) {
          
            Element partNode = (Element) partList.item(0);
            rolePositions(partNode.getElementsByTagName("part"));
         }
         marker = new Marker[length];
         for (int i = 0; i < length; i++) {
            marker[i] = new Marker();
            moveMarker(i);
         }
         makeRoles();
         used = 0;
      }
   } 
   
   // Finds locations of each take marker
   private void findTakes(NodeList takes) {
      int num = 0;
      for (int i = 0; i < length; i++){
         Node curTake = takes.item(i);
         if (curTake.getNodeType() == Node.ELEMENT_NODE) {
            Element takeNode = (Element)curTake;   
            NodeList dimensionsList = takeNode.getElementsByTagName("area");
            Element dimensions = (Element) dimensionsList.item(0);
            shots[num][0] = Integer.parseInt(dimensions.getAttribute("x"));
            shots[num][1] = Integer.parseInt(dimensions.getAttribute("y"));
            shots[num][2] = Integer.parseInt(dimensions.getAttribute("h"));
            shots[num][3] = Integer.parseInt(dimensions.getAttribute("w"));
            ++num;
         }
      }
   }
   
   // Maps role names to their positions
   private void rolePositions(NodeList partList) {
      for (int i = 0; i < partList.getLength(); ++i){
         Node curRole = partList.item(i);
         if (curRole.getNodeType() == Node.ELEMENT_NODE) {
            Element role = (Element) curRole;
            String name = role.getAttribute("name").toLowerCase();
            NodeList dimensionList = role.getElementsByTagName("area");
            Element dimensions = (Element) dimensionList.item(0);
            int[] area = new int[4];
            area[0] = Integer.parseInt(dimensions.getAttribute("x"));
            area[1] = Integer.parseInt(dimensions.getAttribute("y"));
            area[2] = Integer.parseInt(dimensions.getAttribute("h"));
            area[3] = Integer.parseInt(dimensions.getAttribute("w"));
            roles.put(name, area);
         }
      }
   
   }
   
   // Constructs roles, uses positions from eariler to allign them
   private void makeRoles() {
      clickRoles = new HashMap<String, RoleView>(); 
      for (String name : roles.keySet()) {
         int[] noChange = {0,0};
         RoleView current = new RoleView(name, ctrl, noChange);
         int[] bounds = roles.get(name);
         current.setPosition(bounds);
         clickRoles.put(name, current);
      }  
   }
   
   public Map<String, RoleView> getRoles() {
      return clickRoles;
   }
   
   // Modified from root to allaign on the room name
   public int[] getPlayerLoc() {
      int[] shift = {area[0] + 40, area[1] + 120};
      shift[0] = shift[0] + (40 * (players - 1));
      return shift;
   }
   
   // Positions a given marker
   public void moveMarker(int num) {
      int[] position = shots[num];
      marker[num].move(position);
   } 
   
   public Marker[] getMarkers() {
      return marker;
   }
   
   public void removeShot() {
      marker[used].hideMarker();
      used++;
   }
   
   // Makes all Markers visible again
   public void reset() {
      for (Marker current : marker) {
         current.showMarker();
      }
      used = 0;
   }
   
   public int[] getRolePosition(String name) {
      return roles.get(name);
   }
     
   
}