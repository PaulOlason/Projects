/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Image representing a shot marker in the
 * view. Can be hidden or desplayed depending
 * on whether or not the shot is in play.
 ********************************************/

package view;

import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import org.w3c.dom.*;

public class Marker extends JLabel {

   public Marker() {
      setMarker();
      showMarker();
   }
   
   //Sets the marker to the correct image
   private void setMarker() {
      Resources r = Resources.getInstance();
      setIcon(r.getMarker());
   }
   
   public void  showMarker() {
      setVisible(true);
   }
   
   public void hideMarker() {
      setVisible(false);
   }
   
   public void move(int[] position) {
      setBounds(position[0],position[1],42,42);  
   }  

}