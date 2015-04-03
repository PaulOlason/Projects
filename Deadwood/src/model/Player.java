/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Player in Deadwood. Represents a user.
 * Has an associated rank, and can hold money
 * and credit. Each Player will be associated
 * with a Room, and can take and perform
 * Roles of equal or lower rank.
 ********************************************/

package model;

public class Player {
   private int id;         //Player's position in order
   private String name;    //Name associated with player
   private int credits;    //Credit ballance
   private int rank;       //Indicates what scenes can be performed
   private int money;      //Dollar ballance
   private Room location;  //Current Room
   private Role role;      //Role if performing

   public Player(int id, int credits, int rank) {
      this.id = id;
      this.credits = credits;
      this.rank = rank;
      money = 0;
      role = null;
   }
   
   // Relocates the player to Room if the move is possible
   // Returns a boolean indication of move was successful
   public boolean move(Room room) {
      if (isInRole()) {
         System.out.println("Currently in role, cannot move.");
         return false;
      }
      if (location.isNeighbor(room.getName())) {
         setLocation(room);
         return true;
      } else {
         System.out.println("Location cannot be reached currently.");
         return false;
      }
   }
   
   // Sets role, assigns Player to role unless null is passed
   public void setRole(Role role) {
      this.role = role;
      if (role != null) {
         role.setPlayer(this);
      }
   }
   
   // Tests if money can be modified by amount, and sets
   // money to the result if it isn't negative
   // Returns ture if success
   public boolean changeMoney(int amount) {
      int balance = money + amount;
      if (balance >= 0) {
         money = balance;
         return true;
      } else {
         return false;
      }
   }

   // Tests if credit can be modified by amount, and sets
   // money to the result if it isn't negative
   // Returns ture if success   
   public boolean changeCredit(int amount) {
      int newCredit = credits + amount;
      if (newCredit >= 0) {
         credits = newCredit;
         return true;
      } else {
         return false;
      }
      
   }
   
   // Assigns Player to the Role represented by roleName if possible
   // Returns true when assigned, false and prints error message if not
   public boolean selectRole(String roleName) {
      String message = "Unknown role name.";
      if (isInRole()) {
         message = "Player currently has a role.";
      } else if (!location.hasScene()) {
         message = "No scene being shot.";      
      } else {
         Role[] roles = location.getAllRoles();
         if (roles != null) {
            for (Role current : roles) {
               if (current.getName().toLowerCase().equals(roleName)) {
                  if (!current.canPlay(rank)) {
                     message = "Rank too low to play role.";
                  } else if (current.hasPlayer()) {
                     message = "Role already taken";
                  } else {
                     setRole(current);
                     return true;
                  }
               }
            }
         }
      }
      System.out.println(message);
      return false;
   
   }
   
   // Performs role if possible, and prints a message indicating result
   // Returns 2 if the player acts, 3 if the scene ends, 0 otherwise
   public int playRole() {
      if (!isInRole()) {
         System.out.println("No current role.");
         return 0;
      }
      int isReward = 1;
      boolean act = role.act();
      if (act) {
         isReward++;
      }
      role.pay(act);
      if (act) {
         isReward++;
         System.out.print("Success! You recieved");
         if (role.isLead()) {
            System.out.println(" 2 credits.");
         } else {
            System.out.println(" $1 and 1 credit.");
         }
         isReward = isReward + location.removeShot();
      } else {
         System.out.print("Failure!");
         if (!role.isLead()) {
            System.out.println(" You received $1.");
         } else {
            System.out.println();
         }
      }
      return isReward;
   }
   
   // Returns true if the player can rehearse, and prints the current bonus
   // Prevents the player from rehearsing beyond garenteed success
   public boolean rehearse() {
      if (!isInRole()) {
         System.out.println("Not in role, cannot rehearse.");
         return false;
      }
      boolean result = role.rehearse();
      if (result) {
         System.out.println("Rehearsed: recive +" + role.getRehearsals() +" on future rolls");
      } else {
         System.out.println("Cannot rehearse further.");
      }
      return result;
   }
   
   // Attempts to change player rank
   // Returns true if success, false otherwise
   public boolean changeRank(String type, int rank) {
      return location.rankUp(type, rank, this);
   }
   
   // Calculates players final score
   public int tallyScore() {
      int score = money + credits;
      score = score + (5 * rank);
      return score;
   }

   // Returns a String representation of the players inventory
   public String stats() {
      String stats = "Stats: $" + money + ", " + credits + " credits, " + "rank: " + rank;
      return stats;
   }
   
   // Returns the name and line of the player's role
   public String roleInf() {
      String info = role.getName() + ", ";
      info = info + "\"" + role.getLine() +"\"";
      return info;
   }
   
   public boolean isInLead() {
      return role.isLead();
   }
   
   public void setName(String name) {
      this.name = name;
   }
   
   public String getName() {
      return name;
   }   
   
   public Room getLocation() {
      return location;
   }
   
   public void setRank(int rank) {
      this.rank = rank;
   }
   
   public void setLocation(Room room) {
      location = room;
   }
   
   public boolean isInRole() {
      return (role != null);
   }
   
   public Role getRole() {
      return role;
   }
   
   public int getId() {
      return id;
   }
   
}