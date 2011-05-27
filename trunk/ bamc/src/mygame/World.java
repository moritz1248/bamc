/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.util.Random;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node.*;

/**
 *
 * @author crazysaem
 */
public class World {
    private int LevelSize_max_x,LevelSize_max_y,LevelSize_max_z;
    private int LevelSize_x,LevelSize_y,LevelSize_z;
    private AssetManager assetmanager;
    private Node BlocksNode = new Node();
    private BulletAppState bulletappstate;
    private BlockArray blockarray;
    private float block_size=1f;
    
    public World(AssetManager asset_manager,BulletAppState bulletapp_state)
    {
        LevelSize_x=0; //Breite
        LevelSize_y=0;  //Höhe
        LevelSize_z=0; //Tiefe
        
        assetmanager=asset_manager;
        bulletappstate=bulletapp_state;
    }
    
    public void LoadLevel(String level)
    {
        int x,y,z=0;
        Random rand = new Random();
        //t_max=4;
        
        LevelSize_max_x=12;     //Breite
        LevelSize_max_y=20;     //Höhe
        LevelSize_max_z=12;     //Tiefe   
        
        LevelSize_x=2;     //Breite
        LevelSize_y=10;     //Höhe
        LevelSize_z=3;     //Tiefe
        
        blockarray = new BlockArray(LevelSize_max_x, LevelSize_max_y, LevelSize_max_z, LevelSize_x, LevelSize_y, LevelSize_z,
                                    assetmanager, bulletappstate);
        
        for(y=0;y<LevelSize_y;y++)
        {
            for(z=0;z<LevelSize_z;z++)
            {
                for(x=0;x<LevelSize_x;x++)
                {
                    if(blockarray.BlockVisible(x,y,z))
                    { 
                        blockarray.EnableBlock(x, y, z, rand.nextInt(4) + 0);
                    }
                }
            }
        }
    }
    
    public Vector3f GetPlayerPos()
    {        
        return new Vector3f((LevelSize_x*block_size)/2,(LevelSize_y*block_size)+5,(LevelSize_z*block_size)/2);
    }
    
    public void TryEnableBlock(int x, int y, int z, int type)
    {
        System.out.println("Enabling Block on ("+x+")("+y+")("+z+")");
        blockarray.EnableBlock(x, y, z, type);
    }
    
    public void TryDisableBlock(int x, int y, int z)
    {
        Random rand = new Random();
        
        System.out.println("Disabling Block on ("+x+")("+y+")("+z+")");
        blockarray.DisableBlock(x, y, z);
        
        if(blockarray.BlockVisible(x,y-1,z))
        {
            if(blockarray.GetType(x, y-1, z)!=-1)
                blockarray.EnableBlock(x, y-1, z, blockarray.GetType(x, y-1, z));
            else
                blockarray.EnableBlock(x, y-1, z, rand.nextInt(4) + 0);
        }
        
        if(blockarray.BlockVisible(x,y+1,z))
        {
            if(blockarray.GetType(x, y+1, z)!=-1)
                blockarray.EnableBlock(x, y+1, z, blockarray.GetType(x, y+1, z));
            else
                blockarray.EnableBlock(x, y+1, z, rand.nextInt(4) + 0);
        }
        
        if(blockarray.BlockVisible(x-1,y,z))
        {
            if(blockarray.GetType(x-1, y, z)!=-1)
                blockarray.EnableBlock(x-1, y, z, blockarray.GetType(x-1, y, z));
            else
                blockarray.EnableBlock(x-1, y, z, rand.nextInt(4) + 0);
        }
        
        if(blockarray.BlockVisible(x+1,y,z))
        {
            if(blockarray.GetType(x+1, y, z)!=-1)
                blockarray.EnableBlock(x+1, y, z, blockarray.GetType(x+1, y, z));
            else
                blockarray.EnableBlock(x+1, y, z, rand.nextInt(4) + 0);
        }
        
        if(blockarray.BlockVisible(x,y,z-1))
        {
            if(blockarray.GetType(x, y, z-1)!=-1)
                blockarray.EnableBlock(x, y, z-1, blockarray.GetType(x, y, z-1));
            else
                blockarray.EnableBlock(x, y, z-1, rand.nextInt(4) + 0);
        }
        
        if(blockarray.BlockVisible(x,y,z+1))
        {
            if(blockarray.GetType(x, y, z+1)!=-1)
                blockarray.EnableBlock(x, y, z+1, blockarray.GetType(x, y, z+1));
            else
                blockarray.EnableBlock(x, y, z+1, rand.nextInt(4) + 0);
        }
    }
    
    public Node CreateNode()
    {            
        BlocksNode.attachChild(blockarray.GetGeom());
        return BlocksNode;
    }
    
    public Vector3f RayCastBlock(Vector3f loc, Vector3f dir_, int blocklength, Line line)
    {
        return blockarray.RayCastBlock(loc, dir_, blocklength, line);
    }
    
}
