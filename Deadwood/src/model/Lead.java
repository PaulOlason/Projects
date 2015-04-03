/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Subclass of Role. Associated with Scenes.
 * Remain in play only as long as cards are
 * in use. Must be performed for rewards to
 * be distributed.
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;


public class Lead extends Role {

   public Lead(Element role, int budget){
      super((Element) role, budget);
   }
   
   // Pays credit if act is set to true
   // other wise pays nothing
   public void pay(boolean act) {
      if (act) {
         actor.changeCredit(2);
      }
   }
   
   // Rewards the given amount of money to the
   // actor, and disassociates with the Role
   public void rewardLead(int amount){
      actor.changeMoney(amount);
      actor.setRole(null);
   }
   
   
   public boolean isLead() {
      return true;
   }
   
}