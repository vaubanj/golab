package net.vclj.golab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.vclj.golab.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
