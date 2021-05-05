package net.vclj.golab.test.entity;

import org.junit.jupiter.api.Test;

import net.vclj.golab.entity.Location;

import static org.junit.jupiter.api.Assertions.*;

public class TestLocation {
//	@BeforeAll

  @Test
  public void testToLetter() {
    assertEquals("A", Location.toLetter(1));
    assertEquals("H", Location.toLetter(8));
    assertEquals("J", Location.toLetter(9));
    assertEquals("Z", Location.toLetter(25));
    assertEquals("AA", Location.toLetter(26));
    assertEquals("AH", Location.toLetter(33));
    assertEquals("AJ", Location.toLetter(34));
    assertEquals("CA", Location.toLetter(76));
    assertEquals("CY", Location.toLetter(99));
    assertThrows(IllegalArgumentException.class, () -> {
      Location.toLetter(0);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      Location.toLetter(100);
    });
  }

  @Test
  public void testFromLetter() {
    assertEquals(1, Location.fromLetter("A"));
    assertEquals(8, Location.fromLetter("H"));
    assertEquals(9, Location.fromLetter("J"));
    assertEquals(25, Location.fromLetter("Z"));
    assertEquals(26, Location.fromLetter("AA"));
    assertEquals(33, Location.fromLetter("AH"));
    assertEquals(34, Location.fromLetter("AJ"));
    assertEquals(76, Location.fromLetter("CA"));
    assertEquals(99, Location.fromLetter("CY"));
    assertThrows(IllegalArgumentException.class, () -> {
      Location.fromLetter("I");
    });
    assertThrows(IllegalArgumentException.class, () -> {
      Location.fromLetter("CZ");
    });
  }

  @Test
  public void testToNumber() {
    assertEquals("1", Location.toNumber(1));
    assertEquals("99", Location.toNumber(99));
    assertThrows(IllegalArgumentException.class, () -> {
      Location.toNumber(0);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      Location.toNumber(100);
    });
  }

  @Test
  public void testFromNumber() {
    assertEquals(1, Location.fromNumber("1"));
    assertEquals(99, Location.fromNumber("99"));
    assertThrows(IllegalArgumentException.class, () -> {
      Location.fromNumber("0");
    });
    assertThrows(IllegalArgumentException.class, () -> {
      Location.fromNumber("100");
    });
  }

  @Test
  public void testToString() {
    assertEquals("A1", Location.toString(1, 19));
    assertEquals("A19", Location.toString(19, 19));
    assertEquals("B1", Location.toString(20, 19));
    assertEquals("J1", Location.toString(153, 19));
    assertEquals("K10", Location.toString(181, 19));
    assertEquals("T19", Location.toString(361, 19));
    assertEquals("CY99", Location.toString(9801, 99));
    assertThrows(IllegalArgumentException.class, () -> {
      Location.toString(0, 19);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      Location.toString(362, 19);
    });
  }

  @Test
  public void testFromString() {
    assertEquals(1, Location.fromString("A1", 19));
    assertEquals(19, Location.fromString("A19", 19));
    assertEquals(20, Location.fromString("B1", 19));
    assertEquals(153, Location.fromString("J1", 19));
    assertEquals(181, Location.fromString("K10", 19));
    assertEquals(361, Location.fromString("T19", 19));
    assertEquals(9801, Location.fromString("CY99", 99));
    assertThrows(IllegalArgumentException.class, () -> {
      Location.fromString("A0", 19);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      Location.fromString("T20", 19);
    });
  }

  @Test
  public void testConstructor() {
    assertThrows(UnsupportedOperationException.class, () -> {
      new Location();
    });
  }
}
