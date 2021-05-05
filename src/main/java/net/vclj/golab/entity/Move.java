package net.vclj.golab.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Move {
	@Id
	@GeneratedValue(generator="move_id_generator")
	private long id;

	/**
	 * White or Black stores as "W" or "B"
	 */
	@Column(length = 1)
	private String player;

	private int moveNumber;

	@ManyToOne
	private Move previousMove;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "previousMove")
	private List<Move> nextMoves;

	@Column(length=10000)
	private String comment;
}
