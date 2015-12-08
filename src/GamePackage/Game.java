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
    private List<Player> players;
    
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
        maze.printMaze();
    }
    
    public int getGameID() {
        return gameID;
    }
    
    public Player addPlayer(int userid) {
        Player p = new Player(userid, players.size(), 100, -1);
        players.add(p);
        return p;
    }

    @Override
    public GameState getGameState() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleInput(int playerid, List<KeyCode> keys) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player getPlayer(int userid) throws RemoteException {
        
        for(Player p : players) {
            if(p.getUserID() == userid) {
                return p;
            }
        }
        Player p = new Player(userid, players.size(), 100, -1);
        players.add(p);
        return p;
    }

    @Override
    public amazingsharedproject.Block[][] getGrid() throws RemoteException {
        return maze.GetGrid();
    }
}
