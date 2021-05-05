package net.vclj.golab.entity;

/**
 * Tool set to manage legal stone locations on a go board. In order to avoid a
 * useless DBMS table with all the different possible locations and performance
 * issues due to the manipulation of a full Java object for each, a location is
 * an integer. This class is therefore only a collection of static attributes
 * and methods and is not supposed to be instantiated (hence the private
 * visibility of the only constructor).
 * 
 * In order to compute this integer, the first coordinate is changed to an
 * integer (0 for 'A', 1 for 'B' and so on...) and multiplied by the board size.
 * And the second coordinate (already a number) is added to it.
 * 
 * Conveniently, on a standard 19x19 board, A1 is 1, tengen is 100 and T19 is
 * 361. It is important however to understand that the location number can't be
 * interpreted without the relevant board size.
 * 
 * The maximum board size is arbitrarily set to 99 (to limit coordinates to
 * 2-character strings while wider boards are unreasonable in the first place).
 *
 * As is customary, letter coordinate does not allow 'I' to avoid confusion with
 * 'J'. There are therefore 25 letter coordinates.
 */

public class Location {

  static private final int MAX_BOARD_SIZE = 99;

  /**
   * Transform a coordinate integer in letters ('A' for 1, 'AB' for 27...)
   * 
   * @param coord the coordinate to transform
   * @return the corresponding letters in a String
   */
  static public String toLetter(int coord) {
    if (coord < 1 || coord > MAX_BOARD_SIZE) {
      throw new IllegalArgumentException(
          "Location coordinates must be between 1 and " + MAX_BOARD_SIZE + " : " + coord);
    }
    int num1 = (coord - 1) / 25;
    int num2 = (coord - 1) % 25;
    String letters = "";
    if (num1 > 0) {
      letters = Character.toString((char) ('A' + num1 - 1));
    }
    if (num2 < 8) {
      letters += Character.toString((char) ('A' + num2));
    } else {
      letters += Character.toString((char) ('A' + num2 + 1));
    }
    return letters;
  }

  /**
   * Transform a coordinate letters in an integer (1 for 'A', 27 for 'AB'...)
   * 
   * @param letters the coordinate to transform
   * @return the corresponding number as an integer
   */
  static public int fromLetter(String letters) {
    int c1, c2, coord;

    if (letters.length() < 1 || letters.length() > 2) {
      throw new IllegalArgumentException("A Letter coordinate must be made of 1 or 2 characters : '" + letters + "'");
    }
    c1 = letters.charAt(0) - 'A' + 1;
    if (c1 < 1 || c1 == 9 || c1 > 26) {
      throw new IllegalArgumentException(
          "A letter coordinate must made of uppercase letters excluding 'I' : '" + letters + "'");
    }
    if (c1 > 8) {
      c1--;
    }
    coord = c1;
    if (letters.length() == 2) {
      c2 = letters.charAt(1) - 'A' + 1;
      if (c2 < 1 || c2 == 9 || c2 > 26) {
        throw new IllegalArgumentException(
            "A letter coordinate must made of uppercase letters excluding 'I' : '" + letters + "'");
      }
      if (c2 > 8) {
        c2--;
      }
      coord = coord * 25 + c2;
    }
    if (coord < 1 || coord > MAX_BOARD_SIZE) {
      throw new IllegalArgumentException("A letter coordinate must be between 'A' and 'CY' : '" + letters + "'");
    }
    return coord;
  }

  static public String toNumber(int coord) {
    if (coord < 1 || coord > MAX_BOARD_SIZE) {
      throw new IllegalArgumentException("A board coordinate must be between 1 and " + MAX_BOARD_SIZE + " : " + coord);
    }
    return Integer.toString(coord);
  }

  static public int fromNumber(String number) {
    int coord;
    try {
      coord = Integer.valueOf(number);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("A number coordinate must be a valid integer : " + number);
    }
    if (coord < 1 || coord > MAX_BOARD_SIZE) {
      throw new IllegalArgumentException(
          "A board coordinate must be bet<ween 1 and " + MAX_BOARD_SIZE + " : " + number);
    }
    return coord;
  }

  static public String toString(int location, int boardSize) {
    if (boardSize < 1 || boardSize > MAX_BOARD_SIZE) {
      throw new IllegalArgumentException("A board size must be bet<ween 1 and " + MAX_BOARD_SIZE + " : " + boardSize);
    }
    int coord1 = (location - 1) / boardSize + 1;
    int coord2 = (location - 1) % boardSize + 1;
    if (coord1 < 1 || coord1 > boardSize || coord2 < 1 || coord2 > boardSize) {
      throw new IllegalArgumentException(
          "Location not valid for selected board size (" + boardSize + ") : " + location);
    }
    return toLetter(coord1) + toNumber(coord2);
  }

  static public int fromString(String location, int boardSize) {
    int coord1, coord2;
    if (boardSize < 1 || boardSize > MAX_BOARD_SIZE) {
      throw new IllegalArgumentException("A board size must be bet<ween 1 and " + MAX_BOARD_SIZE + " : " + boardSize);
    }
    if (location.length() < 2 || location.length() > 4) {
      throw new IllegalArgumentException("A location must be made of 2 to 4 characters : '" + location + "'");
    }
    if (location.charAt(1) >= 'A' && location.charAt(1) <= 'Z') {
      coord1 = fromLetter(location.substring(0, 2));
      coord2 = fromNumber(location.substring(2));
    } else {
      coord1 = fromLetter(location.substring(0, 1));
      coord2 = fromNumber(location.substring(1));
    }
    if (coord1 < 1 || coord1 > boardSize || coord2 < 1 || coord2 > boardSize) {
      throw new IllegalArgumentException(
          "Location not valid for selected board size (" + boardSize + ") : " + location);
    }
    return (coord1 - 1) * boardSize + coord2;
  }

  public Location() {
    throw new UnsupportedOperationException("Location object is not meant to be instantiated");
  }
}
