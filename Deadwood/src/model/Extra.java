/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Subclass of Role. Associated with Sets.
 * Remain in play for duration of game.
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;

public class Extra extends Role {

   public Extra(Element role, int budget){
      super((Element) role, budget);
   }
   
   // Pays player depending on the value of act, which
   // represents if the previous role succeeded or not
   public void pay(boolean act) {
      actor.changeMoney(1);
      if (act) {
         actor.changeCredit(1);
      }
   }
   
   // Rewards extra the roles rank in money if a
   // Lead role was also in use in the Set
   public void rewardExtra(boolean leads) {
      if (leads) {
         actor.changeMoney(rank);
      }
      dropRole();
   }
   
   // Disassociates the current Player from the Role
   // Extras are persistant, so also needs to drop player
   public void dropRole() {
      super.dropRole();
      actor = null;
   }
   
   public void setBudget(int budget) {
      this.budget = budget;
   }

}