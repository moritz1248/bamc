/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.Random;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node.*;

/**
 *
 * @author crazysaem
 */
public class World {
    private int LevelSize_max_x,LevelSize_max_y,LevelSize_max_z;
    private int LevelSize_x,LevelSize_y,LevelSize_z;
    private AssetManager assetmanager;
    //private int[][][] BlockArray;
    //private Block[][][] BlockArray;
    //private int t_max, c;
    private Node BlocksNode = new Node();
    //Geometry geom;
    private BulletAppState bulletappstate;
    //private RigidBody Blocks_phy,Blocks_phy2;
    //private RigidBodyControl Blocks_phy,Blocks_phy2;
    //private Node phy_node;
    //private CompoundCollisionShape Blocks_phy_dynamic;
    //private BoxCollisionShape boxcollisionshape;
    private BlockArray blockarray;
    
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
        
        LevelSize_max_x=1;     //Breite
        LevelSize_max_y=1;     //Höhe
        LevelSize_max_z=1;     //Tiefe   
        
        LevelSize_x=1;     //Breite
        LevelSize_y=1;     //Höhe
        LevelSize_z=1;     //Tiefe
        
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
    
    public Node CreateNode()
    {            
        //return blockarray.GetGeom();
        BlocksNode.attachChild(blockarray.GetGeom());
        return BlocksNode;
    }
    
}
