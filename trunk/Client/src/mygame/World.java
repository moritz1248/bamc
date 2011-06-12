/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.util.Random;
//import com.jme3.bullet.BulletAppState;
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
    //private BulletAppState bulletappstate;
    //private BlockArray blockarray;
    private BlockGeom blockgeom;
    private float block_size=1f;
    
    public World(AssetManager asset_manager)
    {
        LevelSize_x=0; //Breite
        LevelSize_y=0;  //Höhe
        LevelSize_z=0; //Tiefe
        
        assetmanager=asset_manager;
        //bulletappstate=bulletapp_state;
    }
    
    public int GetXMax()
    {
        return LevelSize_max_x;
    }
    
    public int GetYMax()
    {
        return LevelSize_max_y;
    }
    
    public int GetZMax()
    {
        return LevelSize_max_z;
    }
    
    public byte GetBlockType(int x, int y, int z)
    {
        return blockgeom.GetBlockType(x, y, z);
    }
    
    public void LoadLevel(String level)
    {
        int x,y,z=0;
        //Random rand = new Random();
        //t_max=4;
        
        LevelSize_max_x=400;     //Breite
        LevelSize_max_y=400;     //Höhe
        LevelSize_max_z=400;     //Tiefe   
        
        LevelSize_x=20;     //Breite
        LevelSize_y=20;     //Höhe
        LevelSize_z=20;     //Tiefe
        
        //blockarray = new BlockArray(LevelSize_max_x, LevelSize_max_y, LevelSize_max_z, LevelSize_x, LevelSize_y, LevelSize_z, assetmanager);
        
        blockgeom = new BlockGeom(LevelSize_x, LevelSize_y, LevelSize_z, LevelSize_max_x, LevelSize_max_y, LevelSize_max_z, assetmanager);
        
        /*
        for(y=300;y<LevelSize_max_y;y++)
        {
            for(z=0;z<LevelSize_max_z;z++)
            {
                for(x=0;x<LevelSize_max_x;x++)
                {
                    if(blockgeom.BlockVisible(x,y,z))
                    { 
                        //blockarray.EnableBlock(x, y, z, rand.nextInt(4) + 0);
                        blockgeom.SetBlockType(x, y, z, (byte)-1);
                    }
                }
            }
        }
        */
        //blockarray.UpdateGeometry();
        
    }
    
    public void SetBlockType(int x, int y, int z, byte type)
    {
        blockgeom.SetBlockType(x, y, z, type);
        blockgeom.ForecUpdate();
    }
    
    public void SetPlayerPos(Vector3f p)
    {        
        blockgeom.SetPlayerPos(p);
    }
    /*
    public void ForceUpdate()
    {
        blockgeom.ForecUpdate();
    }
    */
    public BlockGeom GetBlockGeom()
    {
        //blockgeom.start();
        //blockgeom.run();
        return blockgeom;
    }
    
    public Vector3f GetPlayerPos()
    {        
        return new Vector3f(LevelSize_max_x/2,LevelSize_max_y/2+10,LevelSize_max_z/2);
    }
    
    /*
    public void TryEnableBlock(int x, int y, int z, int type)
    {
        System.out.println("Enabling Block on ("+x+")("+y+")("+z+")");
        blockarray.EnableBlock(x, y, z, type);
        blockarray.UpdateGeometry();
    }
    */
    /*
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
        
        blockarray.UpdateGeometry();
    }
    */
    public Node CreateNode()
    {            
        BlocksNode.attachChild(blockgeom.GetGeom());
        return BlocksNode;
    }
    /*
    public Vector3f RayCastBlock(Vector3f loc, Vector3f dir_, int blocklength)
    {
        return blockgeom.RayCastBlock(loc, dir_, blocklength);
    }
    */
    public Boolean BlockVisible(int x,int y,int z)
    {
        return blockgeom.BlockVisible(x, y, z);
    }
    
    public Vector3f RayCastBlock(Vector3f loc, Vector3f dir_, int blocklength, boolean add)
    {
        int check_x=0,check_y=0,check_z=0;
        Vector3f pt = new Vector3f();        
        pt.zero();     
        Vector3f dir = new Vector3f();        
        dir.zero();
        
        blocklength*=10;        
        //dir_.normalize();  
        dir_.x*=0.1f;dir_.y*=0.1f;dir_.z*=0.1f;
        //loc.x/=block_size;loc.y/=block_size;loc.z/=block_size;
        
        //Vector3f output = new Vector3f();
        //System.out.println("-------------------START------------------");
        for(int i=1;i<(blocklength+1);i++)
        {
            dir.x=dir_.x;dir.y=dir_.y;dir.z=dir_.z;
            dir.x*=i;dir.y*=i;dir.z*=i;
            pt.x=loc.x+dir.x;pt.y=loc.y+dir.y;pt.z=loc.z+dir.z;
            //pt.x+=dir.x;pt.y+=dir.y;pt.z+=dir.z;
            
            //pt.x/=block_size;
            //pt.y/=block_size;
            //pt.z/=block_size;
            
            
            check_x=(int)(pt.x);
            check_y=(int)(pt.y);
            check_z=(int)(pt.z);
            
            //check_x-=2;
            //check_y-=1;
            //check_z-=1;
            
            
            
            //System.out.println("X: "+check_x+" Y: "+check_y+" Z: "+check_z);
            
            if((check_x>=0) && (check_y>=0) && (check_z>=0) && (check_x<LevelSize_max_x) && (check_y<LevelSize_max_y) && (check_z<LevelSize_max_z))    
            {
                if(blockgeom.GetBlockType(check_x, check_y, check_z)!=-1)
                {
                    //pt.x=check_x;pt.y=check_y;pt.z=check_z;
                    i=blocklength+1;
                    break;
                }
                else
                {
                    pt.x=pt.y=pt.z=-1;
                }
            }
            else
            {
                pt.x=pt.y=pt.z=-1;
            }
        }
        
        if((add==true) && (pt.x!=-1) && (pt.y!=-1) && (pt.z!=-1))
        {
            pt.x=pt.x-dir_.x;            
            pt.y=pt.y-dir_.y;
            pt.z=pt.z-dir_.z;
        }
        
        pt.x=(int)(pt.x);
        pt.y=(int)(pt.y);
        pt.z=(int)(pt.z);   

        return pt;        
    }
    
}
