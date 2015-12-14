/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;

import Maze.Maze;
import Maze.SpawnPoint;
import amazingsharedproject.Ability;
import amazingsharedproject.Block;
import amazingsharedproject.Direction;
import amazingsharedproject.GameState;
import amazingsharedproject.Interfaces.IAbility;
import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Player;
import amazingsharedproject.Used;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String gameName;
    private Maze maze;
    private ArrayList<SpawnPoint> spawnpoints;
    private int spriteSize = 16;
    private int moveSpeed = 20;
    private int attackSpeed = 15;

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
        int idx = 0;
        for (Player p : players) {
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
    public List<Player> getPlayers() throws RemoteException {
        return players;
    }

    @Override
    public void handleInput(int playerid, List<KeyCode> keys) throws RemoteException {
        Player curPlayer = null;
        for (Player p : players) {
            if (p.getID() == playerid) {
                curPlayer = p;
                break;
            }
        }
        if (curPlayer == null) {
            return;
        }

        if (keys.size() < 1) {
            return;
        }

        for (KeyCode key : keys) {
            if (key.isArrowKey()) {
                if (!curPlayer.isMoving()) {
                    movePlayer(curPlayer, key);
                    System.out.println("The following movement key has been pressed: " + keys + " by: " + playerid);
                }
            }
            if (key.isDigitKey()) {
                for (Used ua : usedAbilities) {
                    if (ua.getCreatorID() == curPlayer.getID()) {
                        return;
                    }
                }
                if (key == KeyCode.DIGIT1) {
                    Used u = new Used(curPlayer.getID(), 1, curPlayer.getX(), curPlayer.getY(), curPlayer.getDirection());
                    usedAbilities.add(u);
                    Timer t = new Timer();
                    t.scheduleAtFixedRate(new AbilityAnim(u, curPlayer.getDirection()), 0, attackSpeed);
                }
            }
        }
    }

    public void movePlayer(Player p, KeyCode dir) {
        if (p.isMoving()) {
            return;
        }
        int gridX = (int) (p.getX() / spriteSize);
        int gridY = (int) (p.getY() / spriteSize);
        Block[][] grid = maze.GetGrid();
        Timer t;
        switch (dir) {
            case UP:
                if (grid[gridY - 1][gridX] == Block.SOLID) {
                    System.out.println("Hit wall!");
                    return;
                }
                System.out.println("Moving up!");
                p.setDirection(Direction.UP);
                t = new Timer();
                t.scheduleAtFixedRate(new MoveAnim(p, Direction.UP), 0, moveSpeed);
                break;
            case DOWN:
                if (grid[gridY + 1][gridX] == Block.SOLID) {
                    System.out.print("Hit wall!");
                    return;
                }
                System.out.println("Moving down!");
                p.setDirection(Direction.DOWN);
                t = new Timer();
                t.scheduleAtFixedRate(new MoveAnim(p, Direction.DOWN), 0, moveSpeed);
                break;
            case RIGHT:
                if (grid[gridY][gridX + 1] == Block.SOLID) {
                    System.out.print("Hit wall!");
                    return;
                }
                System.out.println("Moving right!");
                p.setDirection(Direction.RIGHT);
                t = new Timer();
                t.scheduleAtFixedRate(new MoveAnim(p, Direction.RIGHT), 0, moveSpeed);
                break;
            case LEFT:
                if (grid[gridY][gridX - 1] == Block.SOLID) {
                    System.out.print("Hit wall!");
                    return;
                }
                System.out.println("Moving left!");
                p.setDirection(Direction.LEFT);
                t = new Timer();
                t.scheduleAtFixedRate(new MoveAnim(p, Direction.LEFT), 0, moveSpeed);
                break;
        }
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String g) {
        gameName = g;
    }

    private class AbilityAnim extends TimerTask {

        private Used ability;
        private Direction direction;
        private double xEnd;
        private double yEnd;

        private int gridX;
        private int gridY;
        private int idx;

        private boolean destroyed;

        private int addMoves;

        public AbilityAnim(Used ability, Direction direction) throws RemoteException {
            this.ability = ability;
            this.direction = direction;

            /*
             if (direction == Direction.UP) {
             xEnd = ability.getX();
             yEnd = ability.getY() - spriteSize;
             } else if (direction == Direction.DOWN) {
             xEnd = ability.getX();
             yEnd = ability.getY() + spriteSize;
             } else if (direction == Direction.RIGHT) {
             xEnd = ability.getX() + spriteSize;
             yEnd = ability.getY();
             } else if (direction == Direction.LEFT) {
             xEnd = ability.getX() - spriteSize;
             yEnd = ability.getY();
             }
             */
            //idx = spriteSize;
            addMoves = 6;
        }

        @Override
        public void run() {
            double x;
            double y;

            if (destroyed) {
                if (addMoves < 1) {
                    usedAbilities.remove(ability);
                    System.out.println("Ability destroyed");
                    this.cancel();
                } else {
                    addMoves--;
                }

            }

            try {
                x = ability.getX();
                y = ability.getY();
                Block[][] grid = getGrid();

                if (x % 1 == 0 && y % 1 == 0) { //Solid grid position
                    gridX = (int) (ability.getX() / spriteSize);
                    gridY = (int) (ability.getY() / spriteSize);

                    switch (direction) {
                        case UP:
                            if (grid[gridY - 1][gridX] == Block.SOLID) {
                                destroyed = true;
                            }
                            break;
                        case DOWN:
                            if (grid[gridY + 1][gridX] == Block.SOLID) {
                                destroyed = true;
                            }
                            break;
                        case LEFT:
                            if (grid[gridY][gridX - 1] == Block.SOLID) {
                                destroyed = true;
                            }
                            break;
                        case RIGHT:
                            if (grid[gridY][gridX + 1] == Block.SOLID) {
                                destroyed = true;
                            }
                            break;
                    }
                }
                switch (ability.getDirection()) {
                    case UP:
                        ability.setY(y - 1);
                        break;
                    case DOWN:
                        ability.setY(y + 1);
                        break;
                    case LEFT:
                        ability.setX(x - 1);
                        break;
                    case RIGHT:
                        ability.setX(x + 1);
                        break;
                }
                //idx--;
            } catch (RemoteException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            if (d == Direction.UP) {
                xEnd = p.getX();
                yEnd = p.getY() - spriteSize;
            }
            if (d == Direction.DOWN) {
                xEnd = p.getX();
                yEnd = p.getY() + spriteSize;
            }
            if (d == Direction.RIGHT) {
                xEnd = p.getX() + spriteSize;
                yEnd = p.getY();
            }
            if (d == Direction.LEFT) {
                xEnd = p.getX() - spriteSize;
                yEnd = p.getY();
            }
        }

        @Override
        public void run() {

            double x = p.getX();
            double y = p.getY();

            if (x == xEnd && y == yEnd) {
                p.setMoving(false);
                this.cancel();
            }
            if (x < xEnd) {
                p.setX(x + 1);
            }
            if (x > xEnd) {
                p.setX(x - 1);
            }
            if (y < yEnd) {
                p.setY(y + 1);
            }
            if (y > yEnd) {
                p.setY(y - 1);
            }
        }

    }

    @Override
    public Player getPlayer(int userid) throws RemoteException {

        for (Player p : players) {
            if (p.getUserID() == userid) {
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
        for (Player p : players) {
            if (p.getID() == playerid) {
                p.setReady(ready);
                System.out.println("PlayerID: " + p.getID() + " ready: " + ready);
            }
        }

        for (Player p : players) {
            if (p.getReady() == false) //If there is 1 player not ready
            {
                return; //Return [dont start game]
            }
        }

        System.out.println("Starting game!");
        startGame();
    }
}
