package com.roy.bugs;

import com.roy.tracks.Track;

public interface NPC {
    void move();
    void mark();
    Track smell();
    void death();
    void preMove();
    void postMove();
    void border();
    void freeWay();
    void friendHome();
    void friendBud();
    void alienHome();
    void alienBug();
    void food();
}
