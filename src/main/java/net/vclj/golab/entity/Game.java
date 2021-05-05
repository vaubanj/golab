package net.vclj.golab.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Game {
  @Id
  @GeneratedValue(generator="game_id_generator")
  private long id;

  @OneToOne
  private Move firstMove;

  @ElementCollection
  private Map<String, String> properties = new HashMap<String, String>();
}
