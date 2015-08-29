package com.example.bbnsu_000.pokerwifip2p.Game;

/**
 * Created by bbnsu_000 on 8/29/2015.
 */
public class Player {
    String name;
    int money;
    int bet;
    public Player(String name,int  money) {
        this.name = name;
        this.money = money;
        this.bet = 0;


    }
    @Override
    public String toString(){
        return name+","+money;
    }
}
