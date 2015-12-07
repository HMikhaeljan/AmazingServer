/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.rmi.RemoteException;

/**
 *
 * @author Hovsep
 */
public interface IAbility {

    public Double getX() throws RemoteException;

    public Double getY() throws RemoteException;

    public int getCreatorID() throws RemoteException;

}
