package com.springtutorial.session.session.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Data
@RedisHash("Game")
public class Game  {

    public enum STATUS {
        SETUP,
        RUNNING,
        END;
    }

    private @Id String id;
    private @Indexed String hostName;
    private @Indexed String gameName;
    private @Indexed STATUS status;

    private List<String> players = new ArrayList<>();

    public Game(String hostName, String gameName) {
        this.hostName = hostName;
        this.gameName = gameName;
        this.status = STATUS.SETUP;

        players.add(hostName);
    }


}
