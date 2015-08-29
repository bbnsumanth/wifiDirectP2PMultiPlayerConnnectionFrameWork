package com.example.bbnsu_000.pokerwifip2p.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbnsu_000 on 8/29/2015.
 */
public class GameState {
ArrayList<Player> players;
//add other info like cards state etc.
    public GameState(){
        this.players = new ArrayList<Player>();
    }
    public void addPlayer(Player player){
        players.add(player);
    }
    public ArrayList<Player>  getPlayers(){
        return players;
    }

}
