/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Represents either an extra or lead role
 * in the view. Uses a modifier to adjust
 * its location when on a card
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

public class RoleView extends JLabel {
   private String name;       //Name of role
   private Controller ctrl;   //Deadwood controller
   private int[] modifier;    //Amount position is adjusted by

   public RoleView(String name, Controller ctrl, int[] modifier) {
      this.name = name;
      this.ctrl = ctrl;
      this.modifier = modifier;
      addMouseListener(
      new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          signalTakeRole();
        }
      });
   }
   
   public void setPosition(int[] bounds) {
      setBounds(bounds[0] + modifier[0], bounds[1] + modifier[1], 40, 40);
      setVisible(true);
   
   }
   
   public int[] getMod() {
      return modifier;
   }
   
   //Called when role is clicked
   public void signalTakeRole() {
      ctrl.takeRole(name);
   }


}