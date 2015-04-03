/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Contained in the office. Signals to level
 * up player when selected. Represent various
 * prices for specific ranks.
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

public class RankView extends JLabel {
   private int rank;
   private String currency;
   private Controller ctrl;
   
   public RankView(int rank, String currency, Controller ctrl) {
      this.rank = rank;
      this.currency = currency;
      this.ctrl = ctrl;
      addMouseListener(
      new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          signalUpgrade();
        }
      });
      setVisible(true);
   }
   
   public void setLocation(int[] loc) {
      setBounds(loc[0], loc[1], loc[2], loc[3]);
      setVisible(true);
   }
   
   //Called when clicked
   public void signalUpgrade() {
      ctrl.upgrade(rank, currency);
   
   }


}