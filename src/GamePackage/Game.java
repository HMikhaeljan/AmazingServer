/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;

import Maze.Maze;
import Maze.SpawnPoint;
import amazingsharedproject.Block;
import amazingsharedproject.Direction;
import amazingsharedproject.GameState;
import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Player;
import amazingsharedproject.Used;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

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
    private int moveSpeed = 10;

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
        Player curPlayer = null;
        for(Player p: players) {
            if(p.getID() == playerid)
            {
                curPlayer = p;
                break;
            }
        }
        if(curPlayer == null)
            return;
        
        if(keys.size() < 1)
            return;
        System.out.println("The following keys have been pressed: " + keys + " by: " + playerid);
        
        for(KeyCode key: keys) {
            if(key.isArrowKey()) {
                if(!curPlayer.isMoving())
                    movePlayer(curPlayer, key);
            }
        }
    }
    
    
    public void movePlayer(Player p, KeyCode dir) {    
        if(p.isMoving())
            return;
        int gridX =(int) (p.getX() / spriteSize);
        int gridY =(int) (p.getY() / spriteSize);
        Block[][] grid = maze.GetGrid();
        
        switch(dir) {
            case UP:
                if(grid[gridY-1][gridX] == Block.SOLID) {
                    System.out.println("Hit wall!");
                    return;
                }
                System.out.println("Moving up!"); 
                p.setDirection(Direction.UP);
                Timer t = new Timer();
                t.scheduleAtFixedRate(new MoveAnim(p, Direction.UP), 0, moveSpeed);
                break;
        }
    }
    
    private class MoveAnim extends TimerTask {
        private Player p;
        private int moves;
        private Direction d;
        private double xEnd;
        private double yEnd;
        public MoveAnim(Player p, Direction d) {
            this.p = p;
            this.d = d;
            moves = 0;
            p.setMoving(true);
            if(d == Direction.UP)
            {
                xEnd = p.getX();
                yEnd = p.getY() - spriteSize;
            }
            
        }

        @Override
        public void run() {
            
            double x = p.getX();
            double y = p.getY();
            
            if(x == xEnd && y == yEnd) {
                p.setMoving(false);
                this.cancel();
            }
            if(x < xEnd) {
                p.setX(x + 1);
            }
            if(x > xEnd) {
                p.setX(x - 1);
            }
            if(y < yEnd)
            {
                p.setY(y + 1);
            }
            if(y > yEnd) {
                p.setY(y - 1);
            }
        }
        
    }
    
    @Override
    public Player getPlayer(int userid) throws RemoteException {
        
        for(Player p : players) {
            if(p.getUserID() == userid) {
                return p;
            }
        }
        //System.out.println("Bananen zijn cool");
        Player p = new Player(userid, players.size(), 100, 1);
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
