/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserPackage;

import java.io.Serializable;

/**
 *
 * @author Hovsep
 */
public class User implements Serializable {

    private int userID;
    private String name;
    private String password;
    private Stat stats;

    /**
     * Constructor for User If stats is null stats must be created.
     *
     * @param userID Cannot be null and must be unique.
     * @param name Cannot be null and must be unique.
     * @param email Cannot be null.
     * @param stats can be null.
     */
    public User(int userID, String name, String password, Stat stats) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.stats = stats;
    }

    public Stat getStats(){
        return stats;
    }
    public int getStatID() {
        return stats.getStatsID();
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public String setEmail(String password) {
        return this.password = password;
    }
    /**
     * Changes stats after a game ended.
     *
     * @param kills how many players were killed by player.
     * @param deaths how many times player died.
     * @param win if the game is won must be true, lost is false;
     */
    public void changeStats(int kills, int deaths, boolean win) {

    }  
}
