/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Board for Deadwood. References all
 * Players, Rooms, and a Deck of all scenes.
 * Tracks when days begin and end.
 ********************************************/
package model;

import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;

import view.Resources;
import view.BoardView;
import view.SidePanel;
import controller.Controller;

public class Board {
   private Deck deck;               //Contains all cards
   private int days;                //Days remaining
   private Player[] players;        //List of all players
   private int curPlayer;           //Index of current player
   private Map<String, Room> rooms; //Maps rooms to their name
   private int cards;               //Number of cards in play
   private int noPlayers;           //Total number of players
   private JFrame frame;            //Image frame
   private BoardView boardView;     //Used to modify view
   private int actions;             //Number of actions performed
   private boolean done;            //Indicates if game is over
   

   public Board(int noPlayers, boolean shuffle) {
      Controller ctrl = new Controller(this);
      makeFrame(ctrl);
      actions = 0;
      this.noPlayers = noPlayers;
      days = 4;
      curPlayer = 0;
      players = new Player[noPlayers];
      rooms = new HashMap<String, Room>();
      makeRooms();
      createPlayers(noPlayers);
      makeDeck();
      if (shuffle) {
         deck.shuffle();
      }
      deal(); 
   }
   
   // Constructs the frame
   private void makeFrame(Controller ctrl) {
      frame = new JFrame();
      frame.setTitle("Deadwood");
      frame.setSize(1400,1400);
      boardView = new BoardView(ctrl);
      frame.getContentPane().add(boardView);
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }});
   }
   
   // Sets frame to be visible, assures JLabels in correct order
   public void activateFrame() {
      Player player = currentPlayerNoChange();
      String name = player.getName();
      String stats = player.stats();
      boardView.activate(name, stats);
      frame.setVisible(true);
   }
   
   // Creates each player object and stores to players
   private void createPlayers(int noPlay) {
      int[] stats = createPlayerMod(noPlay);
      for (int i = 0; i < noPlay; i++) {
         players[i] = new Player(i + 1, stats[0], stats[1]);
         players[i].setLocation(getRoom("trailer"));
      }
      boardView.addPlayers(noPlay, stats[1]);
   }
   
   // Modifies rules depending on noPlay
   // Returns an array containing the change in credits
   // and rank depending on player count
   private int[] createPlayerMod(int noPlay) {
      int[] stats = new int[2];
      stats[0] = 0;
      stats[1] = 1;   
      switch (noPlay) {
         case 2: case 3:
            days--;
            return stats;
         case 5:
            stats[0] = 2;
            return stats;
         case 6:
            stats[0] = 4;
            return stats;
         case 7: case 8:
            stats[1] = 2;
            return stats;
      }
      
      return stats;
   }
   
   // Builds each room from the rooms.xml file
   // Exits if rooms.xml cannot be found
   private void makeRooms() {
      try {
         FileInputStream file = new FileInputStream("../resources/rooms.xml");
         NodeList list = getNodes(file);
         for (int i = 0; i < list.getLength(); i++) {
            Node roomNode = list.item(i);
            if (roomNode.getNodeType() == Node.ELEMENT_NODE) {
               Element room = (Element) roomNode;
               findRoomType(room);

            } 
         }
         file.close();
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   }
   
   // Identifies the type of each room
   // Constructs and adds them to the rooms map
   private void findRoomType(Element room) {
      switch (room.getNodeName()) {
      case "office":
         boardView.addRoom(room, "office");
         rooms.put("office", new Office(room));
         return;           
      case "trailer":
         boardView.addRoom(room, "trailer");
         rooms.put("trailer", new Trailer(room));
         return;                   
      case "set":
         boardView.addRoom(room, room.getAttribute("name").toLowerCase());
         rooms.put(room.getAttribute("name").toLowerCase(), new Set(room));
         return; 
      }
   
   }
   
   // Constructs the deck using cards.xml
   // Exits if cards.xml is not found
   private void makeDeck() {
      try {
         FileInputStream file = new FileInputStream("../resources/cards.xml");
         deck = new Deck();
         NodeList list = getNodes(file);
         for (int i = 0; i < list.getLength(); ++i){
            Node card = list.item(i);
            if (card.getNodeType() == Node.ELEMENT_NODE) {
               deck.addCard((Element) card);
               boardView.addCards((Element) card);
            }
         }
         file.close();
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   }
   
   // Returns the child nodes of the given file
   // Exits if there is an error searching the file
   private NodeList getNodes(FileInputStream file) {
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document doc = builder.parse(file);
         doc.getDocumentElement().normalize();
         Element root = doc.getDocumentElement();
         return root.getChildNodes();  
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      } 
      return null;
   }
  
   // Assigns a card to each set and resets card count  
   private void deal() {
      cards = 10;
      for (String current : rooms.keySet()) {
         Room room = rooms.get(current);
         if (room.isSet()) {
            Scene card = deck.getCard();
            room.addScene(card);
            String cardName = card.getImage();
            String roomName = room.getName().toLowerCase();
            boardView.positionCard(roomName, cardName);
         }
      }  
   }
   
   // Returns current player without altering curPlayer
   public Player currentPlayerNoChange() {
      return players[curPlayer];
   }
   
   // Cycles through list of players 
   public void changePlayer() {
      curPlayer++;
      if (curPlayer == noPlayers) {
         curPlayer = 0;
      }
      actions = 0;
   }

   // Finds name in rooms map
   public Room getRoom(String name) {
      Room test = rooms.get(name);
      return rooms.get(name);
  
   }
   
   // Resets the board when a day is over
   // Returns true if all days have been completed
   public boolean endOfDay() {
      days--;
      if (days == 0) {
         boardView.message("End of final day.");
         return true;
      }
      boardView.message("End of day.");
      for (Player current : players) {
         boardView.move(current.getId() - 1, "trailer", current.getLocation().getName().toLowerCase());
         current.setLocation(getRoom("trailer")); 
         Role role = current.getRole();
         if (role != null) {
            role.dropRole();
         }
      }
      boardView.removeAll();
      deal();
      boardView.flip();
      boardView.resetMarkers(); //
      return false;
   }
   
   // Tests if the day is over after a scene is completed
   // Returns 1 if the scene has ended, 0 otherwise
   public int sceneEnd() {
      cards--;
      if (cards == 1) {
         return 1;
      }
      return 0;
   }

   // Signals player to act and tests if their scene has ended
   // Returns 1 for failure, 2 for success, 4 for end of scene
   public void playerAct(Player player) {
      Room currentRoom = player.getLocation();
      if (currentRoom.hasScene()) {
         int[] players = currentRoom.getAllPlayers();
         int result = player.playRole();
         if (result > 0) {
            String name = currentRoom.getName().toLowerCase();
            if (result > 1) {
               boardView.removeShot(name);
            }
            actions = 2;
            if (result > 3) {
               for (int i = 0; i < players.length; i++) {
                  if (players[i] != -1) {
                     boardView.leaveRole(players[i] - 1);
                  }
               }
               actions = 4;
            }
         }
      } 
   }
   
   // Attempts to take role and updates view
   // Returns true if role taken, false otherwise
   public boolean playerWork(Player player, String name) {
      if (player.selectRole(name)) {
         int playNum = (player.getId() - 1);
         String room = player.getLocation().getName().toLowerCase(); 
         if (player.isInLead()) {
            String card = player.getLocation().getScene().getImage();
            boardView.takeLead(playNum, card, room, name);
         } else {
            boardView.takeExtra(playNum, room, name);
         }
         return true;
      }
      return false;
   }

   // Prints results of game and identifies winner
   public void results() {
      boardView.message("Game over!");
      String winner  = null;
      int max = 0;
      System.out.println("Final Scores:");
      for (Player current : players) {
         int score = current.tallyScore();
         if (score > max) {
            max = score;
            winner = current.getName();
         } else if (score == max) {
            winner = "tie";
         }
         
      }      
      boardView.finalMessage(winner, max);
   }
   
   // Moves player and updates view
   public boolean move(String name, Player player) { 
      Room room = getRoom(name);
      if (room != null) {         
         String oldRoom = player.getLocation().getName().toLowerCase();
         if(player.move(room)) {
            if (room.getScene() != null) {
               boardView.reveal(room.getScene().getImage());
            }
            boardView.move((player.getId() - 1), name, oldRoom);
            return true;            
         }         
      } 
      return false; 
   }
   

   /* Methods called by controller */

   public void moveClick(String room) {
      Player player = currentPlayerNoChange();
      if (actions > 0) {
         boardView.message("Cannot move this turn.");       
      } else {    
         if (move(room, player)) {
            boardView.message("Moved to " + room + ".");
            actions = 1;   
         }                 
      }      
   }
   
   public void workClick(String role) {
      if (actions > 1) {
         boardView.message("Cannot work this turn.");
      } else {
         Player player = currentPlayerNoChange();
         if(playerWork(player, role)) {
            boardView.message("Performing role.");
            actions = 2;
         } else {
            boardView.message("Unable to work role.");
         }
      }
   }
   
   //
   public int actClick() {
      Player player = currentPlayerNoChange();
      if (actions > 0) {
         boardView.message("Cannot act this turn.");
      } else {
         String scene = null;
         if (player.getLocation().getScene() != null) {
            scene = player.getLocation().getScene().getImage();
         }
         playerAct(player); 
         if (actions == 4) {
            boardView.message("Scene concluded.");
            boardView.removeCard(scene);
         }
         if (actions > 1) {
            boardView.message("Acting.");
         } else {
            boardView.message("Cannot currently act.");
         }
      }
      String name = player.getName();
      String stats = player.stats();
      boardView.updatePlayer(name, stats);
      return actions;   
   }
   
   public void rehearseClick() {
      Player player = currentPlayerNoChange();
      if (actions > 0) {
         boardView.message("Cannot rehearse this turn.");
      } else if (player.rehearse()) {
         boardView.message("Rehearsed role.");
         actions = 2;
      } else {
         boardView.message("Cannot rehearse currently.");
      }
   }
   
   
   public void endTurnClick() {
      changePlayer();
      Player player = currentPlayerNoChange();
      String name = player.getName();
      String stats = player.stats();
      boardView.updatePlayer(name, stats);
      boardView.message("Turn over.");
   }
   
   public void upgradeClick(String type, int rank) {
      Player player = currentPlayerNoChange();
      if (actions > 1) {
         boardView.message("Cannot rank up this turn.");   
      } else if (player.changeRank(type, rank)) {
         boardView.rankUp(player.getId(), rank);
         boardView.message("Rank increased.");
         actions = 2;
      }
   }

}