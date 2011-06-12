/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author crazysaem
 */
public class PlayerData {
    private Vector3f playerpos;
    private Vector3f playerdir;
    private int id;
    
    public PlayerData(int id_)
    {
        playerpos = new Vector3f(0,0,0);  
        playerdir = new Vector3f(0,0,0);  
        id = id_;
    }
    
    public void SetPlayerPos(Vector3f pos)
    {
        playerpos.x = pos.x;
        playerpos.y = pos.y;
        playerpos.z = pos.z;
    }
    
    public void SetPlayerDir(Vector3f dir)
    {
        playerdir.x = dir.x;
        playerdir.y = dir.y;
        playerdir.z = dir.z;
    }
    
    public Vector3f GetPlayerPos()
    {
        return playerpos;
    }
    
    public Vector3f GetPlayerDir()
    {
        return playerdir;
    }
    
    public int GetID()
    {
        return id;
    }
    
}
