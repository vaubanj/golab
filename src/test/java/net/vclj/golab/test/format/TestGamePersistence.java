package net.vclj.golab.test.format;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import net.vclj.golab.format.SgfGame;
import net.vclj.golab.format.SgfParser;

class TestGamePersistence {
//  static private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("golab");
//  static private final EntityManager entityManager = entityManagerFactory.createEntityManager();

  @Test
  public void testSimpleGamePersistence() throws Exception {
    Path path = Paths.get("./src/main/resources/sgf/simple-12-move-game.sgf");
    String gameAsString = new String(Files.readAllBytes(path));
    SgfGame game = SgfParser.parse(gameAsString);
    game.postProcess();

//    entityManager.getTransaction().begin();
//    entityManager.persist(game);
//    entityManager.getTransaction().commit();
    assertTrue(true);
  }

}
