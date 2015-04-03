/********************************************
 * Paul Olason - CSCI 345 - Winter 2015
 *
 * Subclass of room. Players begin in the
 * Trailer at the start of the game, and
 * return to it at the end of every day.
 * Only one Trailer is used per game.
 ********************************************/

package model;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;

public class Trailer extends Room {

   public Trailer(Element room) {
      super(room);
      name = "trailer";
   }

}