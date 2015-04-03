/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Loads and stores all image files from
 * resources, and retrieves them for use.
 ********************************************/

package view;

import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Resources {
   private Map<String, ImageIcon> cards;
   private Map<String, ImageIcon[]> players;
   private ImageIcon board;
   private ImageIcon shot;
   static Resources instance;   

   private Resources() {
      cards = new HashMap<String, ImageIcon>();
      players = new HashMap<String, ImageIcon[]>();
      board = new ImageIcon(getFile("board.jpg"));
      shot = new ImageIcon(getFile("shot.png"));
      for (int i = 0; i < 40; i++) {
         String name = "cards/";
         if (i < 9) {
            name = name + "0";
         }
         name = name + (i + 1) + ".png";
         ImageIcon icon = new ImageIcon(getFile(name));
         cards.put(name, icon);
      } 
      String[] colors = {"b", "c", "g", "o", "p", "r", "v", "y"};
      for(String current : colors) {
         ImageIcon[] dice = new ImageIcon[6];
         for (int i = 0; i < 6; i++) {
            String name = "dice/" + current + (i + 1) + ".png";
            dice[i] = new ImageIcon(getFile(name));
         }
         players.put(current, dice);
      }
   }   

   //Retrieves file from address fileName
   private BufferedImage getFile(String fileName) {
      try {
         return ImageIO.read( new File(String.format("../resources/graphics/%s", fileName)));
      }catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }
      return null;   
   }   

   public static Resources getInstance() {
      if (instance == null) 
         instance = new Resources();
      return instance;
   }
    
   public ImageIcon getBoard() {
      return board;
   }
   
   public ImageIcon getMarker() {
      return shot;
   }
   
   public ImageIcon getPlayer(String color, int rank) {
      ImageIcon[] dice = players.get(color);
      return dice[rank - 1];
   }
   
   public ImageIcon getCard(String name) {
      return cards.get(name);
   }


}