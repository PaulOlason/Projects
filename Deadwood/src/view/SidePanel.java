/********************************************
 *       Paul Olason - Vadim Trushkov 
 *          CSCI 345 - Winter 2015
 *
 * Desplays player stats and infromation
 * about the players actions.
 ********************************************/

package view;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.image.*;
import org.w3c.dom.*;

import controller.Controller;

public class SidePanel extends JLabel implements ActionListener {
   private JButton endTurn;      //Buttons used to deliver
   private JButton rehearse;     //commands to the 
   private JButton act;          //Deadwood controller
   private Controller ctrl;      //Recieves commands
   private JLabel message;       //Delivers status messages to the player
   private JLabel currentPlayer; //Desplays name of current player
   private JLabel playerStats;   //Desplays current players rank, ect

   public SidePanel(Controller ctrl) {
      super();
      this.ctrl = ctrl;
      setLayout(null);
      setSize(200,900);
      setBounds(1200,0,200,900);
      
      act = new JButton("Act");
      add(act);
      act.setBounds(1250,600,100, 30);
      act.addActionListener(this);
      
      endTurn = new JButton("End Turn");
      add(endTurn);
      endTurn.setBounds(1250,700,100, 30);
      endTurn.addActionListener(this);
      
      rehearse = new JButton("Rehearse");
      add(rehearse);
      rehearse.setBounds(1250,650,100, 30);
      rehearse.addActionListener(this);
      
      
      message = new JLabel();
      message.setText("Welcome to Deadwood");
      currentPlayer = new JLabel();
      playerStats = new JLabel();
      add(message);
      add(currentPlayer);
      add(playerStats);
      message.setBounds(1225,100,200,200);
      currentPlayer.setBounds(1225,200,200,200);
      playerStats.setBounds(1215,300,200,200);
      
      
      setVisible(true);
   
   }
   
   public JButton getEndTurn() {
      return endTurn;
   }
   
   public JButton getRehearse() {
      return rehearse;
   }
   
   public JButton getAct() {
      return act;
   }
   
   
   public JLabel getMessage() {
      return message;
   }
   
   public JLabel getPlayerText() {
      return currentPlayer;
   }
   
   public JLabel getPlayerStats() {
      return playerStats;
   }
   
   // Detects when a button is pressed
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == endTurn) {
         ctrl.endTurn();  
      } else if (e.getSource() == act) {
         ctrl.act();
      } else {
         ctrl.rehearse();
      }
   
   }
   
   // Updates the stats of the current player
   public void update(String name, String stats) {
      String message = "Current player is: " + name;
      currentPlayer.setText(message);
      playerStats.setText(stats);
   }
   
   
   // Desplays winner at end of game
   public void finalMessage(String winner, int score) {
      if (!winner.equals("tie")) {
         currentPlayer.setText("The winner is " + winner + "!");
      } else {
        currentPlayer.setText("Tie game. No winner."); 
      }
      playerStats.setText("Final Score: " + score);
      act.setVisible(false);
      rehearse.setVisible(false);
      endTurn.setVisible(false);
      
   }
   
   // Prints the current message in the panel
   public void message(String text) {
      message.setText(text);   
   }

}