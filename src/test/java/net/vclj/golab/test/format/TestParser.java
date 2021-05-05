package net.vclj.golab.test.format;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import net.vclj.golab.format.SgfGame;
import net.vclj.golab.format.SgfGameNode;
import net.vclj.golab.format.SgfParser;

public class TestParser {

  @Test
  public void testSimpleMainLineParsing() throws Exception {
    Path path = Paths.get("./src/main/resources/simple-12-move-game.sgf");
    String gameAsString = new String(Files.readAllBytes(path));
    SgfGame game = SgfParser.parse(gameAsString);
    game.postProcess();
    assertEquals(12, game.getNoMoves());

    SgfGameNode node = game.getFirstMove();

    for (int i = 1; i < 13; i++) {
      assertEquals(i, node.getMoveNo());
      node = node.getNextNode();
    }
    assertEquals(null, node);
  }

  @Test
  public void testMoveNumbers() throws Exception {
    Path path = Paths.get("./src/main/resources/game-branching-complex.sgf");
    String gameAsString = new String(Files.readAllBytes(path));
    SgfGame game = SgfParser.parse(gameAsString);
    game.postProcess();
    assertEquals(6, game.getNoMoves());

    SgfGameNode node = game.getFirstMove();
    assertEquals(1, node.getMoveNo());

    Iterator<SgfGameNode> ite = node.getChildren().iterator();
    SgfGameNode firstChild = ite.next();
    assertEquals(2, firstChild.getMoveNo());
    assertEquals(3, firstChild.getNextNode().getMoveNo());
    assertEquals(4, firstChild.getNextNode().getNextNode().getMoveNo());

    assertEquals(3, firstChild.getChildren().iterator().next().getMoveNo());
    assertEquals(4, firstChild.getChildren().iterator().next().getNextNode().getMoveNo());

    SgfGameNode secondChild = ite.next();
    assertEquals(2, secondChild.getMoveNo());
    assertEquals(3, secondChild.getNextNode().getMoveNo());
    assertEquals(4, secondChild.getNextNode().getNextNode().getMoveNo());

    assertEquals(3, secondChild.getChildren().iterator().next().getMoveNo());
    assertEquals(4, secondChild.getChildren().iterator().next().getNextNode().getMoveNo());

  }

  @Test
  public void testSimpleParsing() throws Exception {
    Path path = Paths.get("./src/main/resources/game-branching-simple.sgf");
    String gameAsString = new String(Files.readAllBytes(path));
    SgfGame game = SgfParser.parse(gameAsString);
    game.postProcess();
    assertEquals(4, game.getNoMoves());

    SgfGameNode node = game.getFirstMove();
    assertEquals(2, node.getChildren().size());
  }
}
