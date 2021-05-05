package net.vclj.golab.format;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SgfFileManager {
  private SgfGame game;

  private SgfFileManager(String sgf) {
    game = SgfParser.parse(sgf);

    game.postProcess();
  }

  public static SgfGame createFromPath(Path path, String charSet) {
    try {
      String gameAsString = new String(Files.readAllBytes(path), charSet);
      return createFromString(gameAsString);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static SgfGame createFromPath(Path path) {
    try {
      String gameAsString = new String(Files.readAllBytes(path), "UTF-8");
      return createFromString(gameAsString);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static SgfGame createFromString(String gameAsString) {
    SgfFileManager rtrn = new SgfFileManager(gameAsString);
    return rtrn.getGame();
  }

  public static SgfGame createFromInputStream(InputStream in) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8").newDecoder()))) {
      StringBuilder out = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        out.append(line);
      }
      SgfFileManager rtrn = new SgfFileManager(out.toString());
      return rtrn.getGame();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public static void writeToFile(SgfGame game, Path destination) {
    writeToFile(game, destination, "UTF-8");
  }

  public static void writeToFile(SgfGame game, Path destination, String encoding, boolean keepOriginal) {
    if (keepOriginal) {
      Path copyOfOriginal = Paths.get(destination.toString() + ".orig." + System.currentTimeMillis());
      try {
        Files.copy(destination, copyOfOriginal);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    writeToFile(game, destination, encoding);
  }

  public static void writeToFile(SgfGame game, Path destination, String encoding) {
    try (
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(destination.toFile()), Charset.forName(encoding).newEncoder())) {
      osw.write(game.toString());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static File writeToFile(String sgf) {
    BufferedOutputStream bos = null;
    try {
      File tmpFile = File.createTempFile("sgf4j-test-", ".sgf");
      bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
      bos.write(sgf.getBytes());
      return tmpFile;
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    finally {
      if (bos != null) {
        try {
          bos.close();
        }
        catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private SgfGame getGame() {
    return game;
  }
}
