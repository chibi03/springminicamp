package com.springtutorial.session.session;

import com.springtutorial.session.session.model.Game;
import com.springtutorial.session.session.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

@RestController("/werewolf")
public class SessionController {

    @Autowired
    private GameRepository repository;


    @RequestMapping("runningGames")
    public

    @RequestMapping("/runningGamesNumber")
    public ResponseEntity<String> getNumberOfGames() {
        Iterable<Game> gameIter = repository.findAll();

        int size = 0;
        for (Iterator<Game> iter = gameIter.iterator(); iter.hasNext();) {
            if(iter.next().getStatus() == Game.STATUS.RUNNING){
                size++;
            }
        }

        return new ResponseEntity<String>("Number of running games: " + size, HttpStatus.OK);
    }


    @RequestMapping("/createGame/{gameName}")
    public ResponseEntity<String> createGame(@RequestParam("firstName") String firstname, @PathVariable("gameName") String gamename) {
        List<Game> games = repository.findByHostName(firstname);
        if (games.size() > 0) {
            return new ResponseEntity<>("You are already hosting a game", HttpStatus.BAD_REQUEST);
        }

        Game newGame = new Game(firstname, gamename);
        repository.save(newGame);

        return new ResponseEntity<>(newGame.getId(), HttpStatus.OK);
    }

    @RequestMapping("/joinGame/{gameName}")
    public ResponseEntity<String> joinGame(@RequestParam("firstName") String firstname, @PathVariable("gameName") String gamename) {
        List<Game> games = repository.findByGameName(gamename);
        if (games.size() != 1) {
            return new ResponseEntity<>("No game with this name found.", HttpStatus.BAD_REQUEST);
        }

        Game current = games.get(0);
        if (current.getPlayers().contains(firstname)) {
            return new ResponseEntity<>("Player with this name already exists", HttpStatus.BAD_REQUEST);
        }

        if (current.getStatus() == Game.STATUS.RUNNING) {
            return new ResponseEntity<>("The game is already running, please choose another");
        }

        current.getPlayers().add(firstname);
        repository.save(current);
        return new ResponseEntity<>(current.getId(), HttpStatus.OK);
    }

    @RequestMapping("/startGame/{gameName}")
    public ResponseEntity<String> startGame(@PathVariable("gameName") String gameName) {
        return updateStatus(gameName, Game.STATUS.RUNNING);
    }

    @RequestMapping("/endGame/{gameName}")
    public ResponseEntity<String> endGame(@PathVariable("gameName") String gameName) {
        return updateStatus(gameName, Game.STATUS.END);
    }

    private ResponseEntity<String> updateStatus(String gameName, Game.STATUS status) {
        String action = "";
        switch (status) {
            case END:
                action = "stopped";
            case SETUP:
                action = "initialized";
            case RUNNING:
                action = "started";
        }

        List<Game> games = repository.findByGameName(gameName);
        if (games.size() != 1) {
            return new ResponseEntity<>("Game could not be " + action, HttpStatus.BAD_REQUEST);
        }

        Game current = games.get(0);
        current.setStatus(status);
        repository.save(current);
        return new ResponseEntity<>("Successfully " + action, HttpStatus.OK);
    }


}
