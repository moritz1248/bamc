/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author crazysaem
 */
public class GameData {
    private LinkedList<PlayerData> playerdata;
    private LinkedList<BlockData> blockdata;
    //private byte[][][] blockarray;    
    private int LevelSize_max_x;
    private int LevelSize_max_y;
    private int LevelSize_max_z;
    
    public GameData()
    {
        //Hardcoded Array size.
        //Pray 2 teh gods, that this will work ... *fingers crossed*
        LevelSize_max_x=400;  //Breite
        LevelSize_max_y=400;  //Höhe
        LevelSize_max_z=400;  //Tiefe 

        int x,y,z;
        
        playerdata = new LinkedList<PlayerData>();
        blockdata = new LinkedList<BlockData>();

        //Random rand = new Random();
        /*
        blockarray = new byte[LevelSize_max_x][LevelSize_max_y][LevelSize_max_z];         

        for(x=0;x<LevelSize_max_x;x++)
            for(y=LevelSize_max_y/2;y<LevelSize_max_y;y++)
                for(z=0;z<LevelSize_max_z;z++)
                    blockarray[x][y][z] = -1;

        for(x=0;x<LevelSize_max_x;x++)
            for(y=0;y<LevelSize_max_y/2;y++)
                for(z=0;z<LevelSize_max_z;z++)
                    blockarray[x][y][z] = (byte)(rand.nextInt(4) + 0);  
         */
    }
    
    public void SetBlockType(int x, int y, int z, byte type)
    {
        if(x<0)
            return;
        if(y<0)
            return;
        if(z<0)
            return;
        if(x>=(LevelSize_max_x))
            return;
        if(y>=(LevelSize_max_y))
            return;
        if(z>=(LevelSize_max_z))
            return;
        
        for(int i=0;i<blockdata.size();i++)
        {
            if(blockdata.get(i).CheckPos(x, y, z))
            {
                blockdata.get(i).SetType(type);
                return;
            }
        }
        
        blockdata.add(new BlockData(x, y, z, type));
    }
    
    public byte GetBlockType(int x, int y, int z)
    {
        for(int i=0;i<blockdata.size();i++)
        {
            if(blockdata.get(i).CheckPos(x, y, z))
            {
                return blockdata.get(i).GetType();
            }
        }
        return -1;
    }
    
    public byte[] GetBlockMessage(byte id)
    {
        byte[] buffer = new byte[blockdata.size()*13+4+1];
        buffer[0] = id;
        
        byte[] size = ByteIntConvert.intToByteArray(blockdata.size());
        
        buffer[1] = size[0];
        buffer[2] = size[1];
        buffer[3] = size[2];
        buffer[4] = size[3];
        
        for(int i=0;i<blockdata.size();i++)
        {
            byte[] x = ByteIntConvert.intToByteArray(blockdata.get(i).GetXPos());
            byte[] y = ByteIntConvert.intToByteArray(blockdata.get(i).GetYPos());
            byte[] z = ByteIntConvert.intToByteArray(blockdata.get(i).GetZPos());
            
            buffer[5+i*13] = x[0];
            buffer[6+i*13] = x[1];
            buffer[7+i*13] = x[2];
            buffer[8+i*13] = x[3];
            
            buffer[9+i*13]  = y[0];
            buffer[10+i*13] = y[1];
            buffer[11+i*13] = y[2];
            buffer[12+i*13] = y[3];
            
            buffer[13+i*13] = z[0];
            buffer[14+i*13] = z[1];
            buffer[15+i*13] = z[2];
            buffer[16+i*13] = z[3];
            
            buffer[17+i*13] = blockdata.get(i).GetType();
        }
        
        return buffer;
    }
    
    public void addplayer(int id)
    {
        PlayerData pdata = new PlayerData(id);
        playerdata.add(pdata);
        System.out.println("Player "+(id)+" joined the Game!"); 
    }
    
    public void removeplayer(int id)
    {
        System.out.println("Player "+(id)+" has left the Game!"); 
        for(int i=0;i<playerdata.size();i++)
        {
            if(playerdata.get(i).GetID()==id)
            {
                playerdata.remove(i);
                break;
            }
        }
        
    }
    
    public void setplayerpos(Vector3f pos, int id)
    {
        for(int i=0;i<playerdata.size();i++)
        {
            if(playerdata.get(i).GetID()==id)
            {
                playerdata.get(i).SetPlayerPos(pos);
            }
        }
    }
    
    public void setplayerdir(Vector3f dir, int id)
    {
        for(int i=0;i<playerdata.size();i++)
        {
            if(playerdata.get(i).GetID()==id)
            {
                playerdata.get(i).SetPlayerDir(dir);
            }
        }
    }
    
    public Vector3f[] getplayerpos()
    {
        Vector3f[] result = new Vector3f[playerdata.size()];
        
        for(int i=0;i<playerdata.size();i++)
        {
            result[i] = playerdata.get(i).GetPlayerPos();
        }    
        
        return result;
    }
    
    public Vector3f[] getplayerdir()
    {
        Vector3f[] result = new Vector3f[playerdata.size()];
        
        for(int i=0;i<playerdata.size();i++)
        {
            result[i] = playerdata.get(i).GetPlayerDir();
        }    
        
        return result;
    }
}
