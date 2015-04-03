/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Manages overall view functions and
 * contains all other elements
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

public class BoardView extends JPanel {
   private JLabel board;
   private PlayerView[] players;
   private Map<String, RoomView> rooms;
   private Map<String, CardView> cards;
   private RoomView trailer;
   private Controller ctrl;
   private SidePanel sidePanel;
   
   
   
   public BoardView(Controller ctrl) {
      super();
      this.ctrl = ctrl;
      setLayout(null);
      Resources r = Resources.getInstance();
      setSize(1200,900);
      board = new JLabel();
      board.setBounds(0,0,1200,900);
      board.setIcon(r.getBoard());
      rooms = new HashMap<String, RoomView>();
      cards = new HashMap<String, CardView>();
      
      sidePanel = new SidePanel(ctrl);
      add(sidePanel.getAct());
      add(sidePanel.getRehearse());
      add(sidePanel.getEndTurn());
      add(sidePanel.getPlayerText());
      add(sidePanel.getPlayerStats());
      add(sidePanel.getMessage());
      add(sidePanel);
      
   }
   
   // Adds lower level elements to panel
   public void activate(String name, String stats) {
      for (String currentRoom : rooms.keySet()) {
         add(rooms.get(currentRoom));
      }
      add(board);
      updatePlayer(name, stats);
      setVisible(true);
   }
   
   // Puts board behind new elements
   public void flip() {
      for (String currentRoom : rooms.keySet()) {
         RoomView roomView = rooms.get(currentRoom);
         remove(roomView);
         add(roomView);
      }
      remove(board);
      add(board);
   }
   
   // Constructs and adds a room depending on its type
   public void addRoom(Element room, String name) {
      RoomView currentRoom;
      if (room.getNodeName() == "set") {
         currentRoom = new SetView(room, name, ctrl);
         for (Marker current : currentRoom.getMarkers()) {
            add(current);
         }
         Map<String, RoleView> roles = currentRoom.getRoles();
         for (String current : roles.keySet()) {
            add(roles.get(current));
         }
      } else if (room.getNodeName() == "office") {
         currentRoom = new OfficeView(room, name, ctrl);
         Map<Integer, RankView> dollars = currentRoom.getDollars();
         for (Integer current : dollars.keySet()) {
            add(dollars.get(current));
         }
         Map<Integer, RankView> credits = currentRoom.getCredits();
         for (Integer current : credits.keySet()) {
            add(credits.get(current));
         }
         
      } else {
         currentRoom = new RoomView(room, name, ctrl);
      }
      rooms.put(name, currentRoom );
      if (name.equals("trailer")) {
         trailer = currentRoom;
      }   
   }
   
   // Adds a player
   public void addPlayers(int noPlayers, int rank) {
      String[] names = {"b", "r", "y", "g", 
         "c", "p", "o", "v"};
      players = new PlayerView[noPlayers];
      for (int i = 0; i < noPlayers; i++) {
         players[i] = new PlayerView(names[i]);
         add(players[i]);
         trailer.addPlayer();
         players[i].move(trailer.getPlayerLoc(), "trailer");
         players[i].savePosition(trailer.getPlayerLoc());
         players[i].setImage(rank);
      }
   }
   
   
   // Relocates a player to a new room
   // Does nothing if room is oldRoom
   public void move(int player, String room, String oldRoom) {
      if (!room.equals(oldRoom)) {
         rooms.get(oldRoom).removePlayer();
         RoomView newRoom = rooms.get(room);
         int[] area = newRoom.getPlayerLoc();
         newRoom.addPlayer();
         players[player].move(area, room);
         players[player].savePosition(area);
     }
   
   }  
   
   // Constructs new CardViews
   public void addCards(Element card) {
      CardView currentCard = new CardView(card, ctrl);
      cards.put(currentCard.getImage(), currentCard);
   }
   
   // Locates cards to positions in rooms
   public void positionCard(String room, String card) {
      RoomView currentRoom = rooms.get(room);
      CardView currentCard = cards.get(card);
      int[] area = currentRoom.getArea();
      currentCard.place(area);
      currentCard.setRoom(room);
      
      Map<String, RoleView> roles = currentCard.getRoles();
      for (String role : roles.keySet()) {
         add(roles.get(role));
      }
      
      add(currentCard);
   
   }
   
   public PlayerView getPlayer(int num) {
      return players[num];
   }
   
   // Changes player image to reflect rank
   public void rankUp(int id, int rank) {
      players[id -1].setImage(rank);
   }
   
   // Resets all markers at the end of day
   public void resetMarkers() {
      for (String name : rooms.keySet()) {
         RoomView room = rooms.get(name);
         room.reset();
      }
   }
   
   public void removeShot(String name) {
      RoomView room = rooms.get(name);
      room.removeShot();
   }
   
   // Puts a player in an extra role
   public void takeExtra(int player, String name, String role) {
      RoomView room = rooms.get(name);
      int[] location = room.getRolePosition(role);
      players[player].move(location, name);
   }
   
   // Puts a player in a lead role
   public void takeLead(int player, String image, String room, String role) {
      CardView card = cards.get(image);
      int[] location = card.getRolePosition(role);
      players[player].move(location, room);
      
   }
   
   // Removes player from a role
   public void leaveRole(int player) {
      players[player].leaveRole(); 
   }
   
   // Updates the side panel stats
   public void updatePlayer(String name, String stats) {
      sidePanel.update(name, stats);
   }
   
   // Removes a card from the board
   public void removeCard(String image){
      CardView card = cards.get(image);
      Map<String, RoleView> roles = card.getRoles();
      for (String role : roles.keySet()) {
         remove(roles.get(role));
      }
      cards.remove(card);
      remove(card);
   }
   
   // Removes all cards from the baord at the end of day
   public void removeAll() {
      for (String image : cards.keySet()) {
         CardView card = cards.get(image);
         Map<String, RoleView> roles = card.getRoles();
         for (String role : roles.keySet()) {
            remove(roles.get(role));
         }
         cards.remove(card);
         remove(card);
      }
   }
   
   // Changes the side panels status message 
   public void message(String text) {
      sidePanel.message(text);
   }
   
   // Final message desplayed at end of day
   public void finalMessage(String winner, int max) {
      sidePanel.finalMessage(winner, max);
   }
   
   public void reveal(String image) {
      cards.get(image).showCard();
   }

}