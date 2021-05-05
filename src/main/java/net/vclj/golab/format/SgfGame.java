package net.vclj.golab.format;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class stores a Go game created by parsing a SGF file. It deals with
 * loading the game and saving the game back to disk.
 */
public class SgfGame {
  private static final Logger log = LoggerFactory.getLogger(SgfGame.class);
  
  private Map<String, String> properties = new HashMap<String, String>();
  
  private SgfGameNode rootNode;
  
  private int noMoves = 0;

  private int noNodes = 0;

  private String originalSgf = null;

  public SgfGame(String sgfString) {
    this.originalSgf = sgfString;
  }

  public void addProperty(String key, String value) {
    /*
     * Actually properties can be set multiple times and it seems based on other
     * software that the expectation is that everything is appended rather than the
     * last definition wins.
     */
    if (properties.get(key) != null) {
      String current = properties.get(key);
      properties.put(key, current + "," + value);
    } else {
      properties.put(key, value);
    }
  }

  public String getProperty(String key) {
    return properties.get(key);
  }

  public String getProperty(String key, String defaultValue) {
    if (properties.get(key) == null) {
      return defaultValue;
    } else {
      return properties.get(key);
    }
  }

  public Map<String, String> getProperties() {
    return new HashMap<String, String>(this.properties);
  }

  public void setRootNode(SgfGameNode rootNode) {
    this.rootNode = rootNode;
  }

  public SgfGameNode getRootNode() {
    return rootNode;
  }

  public int getNoMoves() {
    return noMoves;
  }

  public void setNoMoves(int noMoves) {
    this.noMoves = noMoves;
  }

  public void postProcess() {
    // make sure we have a empty first node
    if (getRootNode().isMove()) {
      SgfGameNode oldRoot = getRootNode();
      SgfGameNode newRoot = new SgfGameNode(null);

      newRoot.addChild(oldRoot);
      setRootNode(newRoot);
    }

    SgfGameNode node = getRootNode();
    // I'll need to figure out if and how to add the heuristical
    // reorder. This can be bad as right now it will have side-effects
    // when saving the game.
    // heuristicalBranchReorder(node);

    // count the moves & nodes
    node = getRootNode();
    do {
      if (node.isMove()) {
        noMoves++;
      }
      noNodes++;
    } while (((node = node.getNextNode()) != null));

    // number all the moves
    numberTheMoves(getRootNode(), 1, 0);
  }

  private void numberTheMoves(SgfGameNode startNode, int moveNo, int nodeNo) {
    SgfGameNode node = startNode;
    int nextMoveNo = moveNo;
    int nextNodeNo = nodeNo;

    if (node.isMove()) {
      startNode.setMoveNo(moveNo);
      nextMoveNo++;
    }

    startNode.setNodeNo(nodeNo);
    nextNodeNo++;

    if (node.getNextNode() != null) {
      numberTheMoves(node.getNextNode(), nextMoveNo, nextNodeNo);
    }

    if (node.hasChildren()) {
      for (Iterator<SgfGameNode> ite = node.getChildren().iterator(); ite.hasNext();) {
        SgfGameNode childNode = ite.next();
        numberTheMoves(childNode, nextMoveNo, nextNodeNo);
      }
    }
  }

  public int getNoNodes() {
    return noNodes;
  }

  public SgfGameNode getFirstMove() {
    SgfGameNode node = getRootNode();

    do {
      if (node.isMove())
        return node;
    } while ((node = node.getNextNode()) != null);

    return null;
  }

  public SgfGameNode getLastMove() {
    SgfGameNode node = getRootNode();
    SgfGameNode rtrn = null;
    do {
      if (node.isMove()) {
        rtrn = node;
      }
    } while ((node = node.getNextNode()) != null);
    return rtrn;
  }

  public boolean isSameGame(SgfGame otherGame) {
    return isSameGame(otherGame, false);
  }

  public boolean isSameGame(SgfGame otherGame, boolean verbose) {
    if (this.equals(otherGame)) {
      if (verbose) {
        System.out.println("The very same game object - returning true");
      }
      return true;
    }

    // all root level properties have to match
    Map<String, String> reReadProps = otherGame.getProperties();
    if (properties.size() != reReadProps.size()) {
      log.trace("Properties mismatch {} {}", properties.size(), otherGame.getProperties().size());
      if (verbose) {
        System.out.printf("Properties mismatch %s %s\n", properties.size(), otherGame.getProperties().size());
      }
      return false;
    }

    for (Iterator<Map.Entry<String, String>> ite = properties.entrySet().iterator(); ite.hasNext();) {
      Map.Entry<String, String> entry = ite.next();
      if (!entry.getValue().equals(reReadProps.get(entry.getKey()))) {
        log.trace("Property mismatch {}={} {}", entry.getKey(), entry.getValue(), reReadProps.get(entry.getKey()));
        if (verbose) {
          System.out.printf("Property mismatch %s='%s' '%s'", entry.getKey(), entry.getValue(),
              reReadProps.get(entry.getKey()));
        }
        return false;
      }
    }

    // same number of nodes?
    if (this.getNoNodes() != otherGame.getNoNodes()) {
      log.trace("Games have different no of nodes {} {}", this.getNoNodes(), otherGame.getNoNodes());
      if (verbose) {
        System.out.printf("Games have different no of nodes old=%s new=%s", this.getNoNodes(), otherGame.getNoNodes());
      }
      return false;
    }

    // same number of moves?
    if (this.getNoMoves() != otherGame.getNoMoves()) {
      log.trace("Games have different no of moves {} {}", this.getNoMoves(), otherGame.getNoMoves());
      if (verbose)
        System.out.println("Games have different number of moves " + this.getNoMoves() + " " + otherGame.getNoMoves());
      return false;
    } else if (verbose) {
      System.out.println("Games have same number of moves " + this.getNoMoves());
    }

    // alrighty, lets check alllllll the moves
    if (!doAllNodesEqual(this, this.getRootNode(), otherGame, otherGame.getRootNode(), verbose)) {
      if (verbose)
        System.out.println("Some nodes don't equal");
      return false;
    }

    return true;
  }

  private boolean doAllNodesEqual(SgfGame game, SgfGameNode node, SgfGame otherGame, SgfGameNode otherNode, boolean verbose) {
    if (!node.isSameNode(otherNode)) {
      if (verbose) {
        System.out.println("Nodes don't equal a=" + node + "\nb=" + otherGame);
      }
      return false;
    }

    // First let's check the nextNode
    SgfGameNode nextNode = node.getNextNode();
    SgfGameNode nextOtherNode = otherNode.getNextNode();

    if (nextNode != null) {
      if (!nextNode.isSameNode(nextOtherNode)) {
        if (verbose) {
          System.out.println("Nodes don't equal");
          System.out.println(nextNode);
          System.out.println(nextOtherNode);
          System.out.println();
        }
        return false;
      }

      if (!doAllNodesEqual(game, nextNode, otherGame, nextOtherNode, verbose)) {
        return false;
      }
    } else if (nextNode == null && nextOtherNode != null) {
      if (verbose) {
        System.out.println("Nodes don't equal node=" + nextNode + " otherNode=" + nextOtherNode);
      }
      return false;
    }

    // Secondly let's check the children nodes
    List<SgfGameNode> children = node.getChildren();
    List<SgfGameNode> otherChildren = otherNode.getChildren();

    if (children.size() != otherChildren.size()) {
      if (verbose) {
        System.out.println("Size of children don't equal node=" + children + " otherNode=" + otherChildren);
      }
      return false;
    }

    for (Iterator<SgfGameNode> ite = children.iterator(); ite.hasNext();) {
      SgfGameNode gameNode = ite.next();
      boolean found = false;
      for (Iterator<SgfGameNode> ite2 = otherChildren.iterator(); ite2.hasNext();) {
        SgfGameNode gameNode2 = ite2.next();
        if (gameNode.isSameNode(gameNode2))
          found = true;
      }
      if (!found) {
        if (verbose) {
          System.out.println("Children don't equal node=" + children + " otherNode=" + otherChildren);
        }
        return false;
      }
    }

    Iterator<SgfGameNode> ite = children.iterator();
    Iterator<SgfGameNode> otherIte = otherChildren.iterator();
    for (; ite.hasNext();) {
      SgfGameNode childNode = ite.next();
      SgfGameNode otherChildNode = otherIte.next();
      if (!doAllNodesEqual(game, childNode, otherGame, otherChildNode, verbose)) {
        return false;
      }
    }

    return true;
  }

  public String getOriginalSgf() {
    return originalSgf;
  }

  public void setOriginalSgf(String originalSgf) {
    this.originalSgf = originalSgf;
  }

  public String toString() {
    StringBuilder rtrn = new StringBuilder();
    rtrn.append("(");

    // lets write all the root node properties
    Map<String, String> props = getProperties();
    if (props.size() > 0) {
      rtrn.append(";");
    }

    for (Iterator<Map.Entry<String, String>> ite = props.entrySet().iterator(); ite.hasNext();) {
      Map.Entry<String, String> entry = ite.next();
      rtrn.append(entry.getKey() + "[" + entry.getValue() + "]");
    }

    populateSgf(getRootNode(), rtrn);

    rtrn.append(")");
    return rtrn.toString();
  }

  private void populateSgf(SgfGameNode node, StringBuilder sgfString) {
    // print out the node
    sgfString.append(";");
    for (Iterator<Map.Entry<String, String>> ite = node.getProperties().entrySet().iterator(); ite.hasNext();) {
      Map.Entry<String, String> entry = ite.next();
      sgfString.append(entry.getKey() + "[" + entry.getValue() + "]");
    }
    sgfString.append("\n");

    // if we have children then first print out the
    // getNextNode() and then the rest of the children
    if (node.hasChildren()) {
      sgfString.append("(");
      populateSgf(node.getNextNode(), sgfString);
      sgfString.append(")");
      sgfString.append("\n");

      for (SgfGameNode childNode : node.getChildren()) {
        sgfString.append("(");
        populateSgf(childNode, sgfString);
        sgfString.append(")");
        sgfString.append("\n");
      }
    }
    // we can just continue with the next elem
    else if (node.getNextNode() != null) {
      populateSgf(node.getNextNode(), sgfString);
    }
  }
}
