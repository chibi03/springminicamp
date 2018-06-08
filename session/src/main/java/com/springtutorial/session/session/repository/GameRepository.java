package com.springtutorial.session.session.repository;

import com.springtutorial.session.session.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, String> {

    List<Game> findByGameName(String gameName);
    List<Game> findByHostName(String hostName);


}
