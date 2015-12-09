/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;

import Maze.Maze;
import Maze.SpawnPoint;
import amazingsharedproject.GameState;
import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Player;
import amazingsharedproject.Used;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;
import jdk.nashorn.internal.ir.Block;

/**
 *
 * @author Hovsep
 */
public class Game extends UnicastRemoteObject implements IGame {
    private int gameID;
    
    //GameState:
    private ArrayList<Player> players;
    private ArrayList<Used> usedAbilities;
    private ArrayList<String> messages;
    
    private Maze maze;
    private ArrayList<SpawnPoint> spawnpoints;
    private int spriteSize = 16;

    /**
     *
     * @param gameID
     * @param gameState
     */
    public Game(int gameID) throws RemoteException {
        this.gameID = gameID;
        this.maze = new Maze(40, 2, 128);
        spawnpoints = maze.getSpawnpoints();
        //maze.printMaze();
        players = new ArrayList<Player>();
        usedAbilities = new ArrayList<Used>();
        messages = new ArrayList<String>();
    }
    
    private void startGame() {
        int idx= 0;
        for(Player p : players) {
            p.setX(spawnpoints.get(idx).getX() * spriteSize);
            p.setY(spawnpoints.get(idx).getY() * spriteSize);
            idx++;
            System.out.println("Player: " + p.getID() + " Spawned at X:" + p.getX() + " Y:" + p.getY());
        }
    }
    
    @Override
    public int getGameID() {
        return gameID;
    }

    @Override
    public GameState getGameState() throws RemoteException {
        GameState gs = new GameState(players, usedAbilities, messages);
        //List<Player> players, List<Used> abilities, List<String> messages
        return gs;
    }

    @Override
    public void handleInput(int playerid, List<KeyCode> keys) throws RemoteException {
        System.out.println("The following keys have been pressed: " + keys + " by: " + playerid);
    }

    @Override
    public Player getPlayer(int userid) throws RemoteException {
        
        for(Player p : players) {
            if(p.getUserID() == userid) {
                return p;
            }
        }
        //System.out.println("Bananen zijn cool");
        Player p = new Player(userid, players.size(), 100, -1);
        players.add(p);
        return p;
    }

    @Override
    public amazingsharedproject.Block[][] getGrid() throws RemoteException {
        return maze.GetGrid();
    }

    @Override
    public void setReady(int playerid, boolean ready) throws RemoteException {
        for(Player p: players) {
            if(p.getID() == playerid) {
                p.setReady(ready);
                System.out.println("PlayerID: " + p.getID() + " ready: " + ready);
            }
        }
        
        for(Player p: players) {
            if(p.getReady() == false) //If there is 1 player not ready
                return; //Return [dont start game]
        }
        
        System.out.println("Starting game!");
        startGame();
    }
}
