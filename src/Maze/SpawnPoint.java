/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maze;

/**
 *
 * @author Robin
 */
public class SpawnPoint 
{
    public final int x;
    public final int y;
    public SpawnPoint(int x, int y)
    {
        this.x =x;
        this.y =y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}