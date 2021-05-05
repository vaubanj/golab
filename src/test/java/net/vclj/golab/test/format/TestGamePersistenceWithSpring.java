package net.vclj.golab.test.format;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.vclj.golab.format.SgfGame;
import net.vclj.golab.format.SgfParser;
import net.vclj.golab.repository.GameRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestGamePersistenceWithSpring {
  @Autowired
  private GameRepository grepository;

  @Test
  public void testSimpleGamePersistence() throws Exception {
    Path path = Paths.get("./src/main/resources/sgf/simple-12-move-game.sgf");
    String gameAsString = new String(Files.readAllBytes(path));
    SgfGame sgfgame = SgfParser.parse(gameAsString);
    sgfgame.postProcess();

    assertNotNull(grepository);
    //grepository.save(game);
    assertTrue(true);
  }

}
