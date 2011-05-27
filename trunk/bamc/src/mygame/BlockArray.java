/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.Random;

/**
 *
 * @author crazysaem
 */
public class BlockArray {
    private int LevelSize_max_x,LevelSize_max_y,LevelSize_max_z;
    //private int LevelSize_x,LevelSize_y,LevelSize_z;
    private Block[][][] BlockArray;
    private float block_size=1f;
    //private Geometry geom;
    private BlockGeom blockgeom;
    private Geometry box_geom;
    private BulletAppState bulletappstate;
    
    BlockArray(int x_max,int y_max, int z_max, int x_,int y_, int z_, AssetManager assetmanager_, BulletAppState bulletappstate_)
    {
        Random rand = new Random();
        int x,y,z;
        Box b = new Box(Vector3f.ZERO, block_size, block_size, block_size);
        box_geom = new Geometry("Box", b);
        
        LevelSize_max_x=x_max;  //Breite
        LevelSize_max_y=y_max;  //Höhe
        LevelSize_max_z=z_max;  //Tiefe
        
        bulletappstate=bulletappstate_;

        BlockArray = new Block[LevelSize_max_x][LevelSize_max_y][LevelSize_max_z];      
        blockgeom = new BlockGeom(LevelSize_max_x, LevelSize_max_y, LevelSize_max_z, assetmanager_);
        
        for(x=0;x<LevelSize_max_x;x++)
            for(y=0;y<LevelSize_max_y;y++)
                for(z=0;z<LevelSize_max_z;z++)
                    BlockArray[x][y][z] = new Block();
        
        for(x=0;x<x_;x++)
            for(y=0;y<y_;y++)
                for(z=0;z<z_;z++)
                    BlockArray[x][y][z].setType(rand.nextInt(4) + 0);
    }
    
    public int GetType(int x, int y, int z)
    {
        if((x<0) || (y<0) || (z<0) || (x>=LevelSize_max_x) || (y>=LevelSize_max_y) || (z>=LevelSize_max_z))
            return -1;
        
        return BlockArray[x][y][z].getType();        
    }
    
    public Geometry GetGeom()
    {
        return blockgeom.GetGeom();
    }
    
    public void EnableBlock(int x, int y, int z, int type)
    {
        /*Safety Check:*/
        if((x<0) || (y<0) || (z<0) || (x>=LevelSize_max_x) || (y>=LevelSize_max_y) || (z>=LevelSize_max_z))
            return;
        
        /*Enable Physics Stuff*/
        box_geom.setLocalTranslation(x*block_size, y*block_size, z*block_size);      
        BlockArray[x][y][z].AddPhy(box_geom);
        BlockArray[x][y][z].setType(type);
        bulletappstate.getPhysicsSpace().add(BlockArray[x][y][z].getPhy());
        
        /*Enable Graphics Stuff*/
        blockgeom.EnableBlock(x, y, z, type);
    }
    
    public void DisableBlock(int x, int y, int z)
    {
        /*Safety Check:*/
        if((x<0) || (y<0) || (z<0) || (x>=LevelSize_max_x) || (y>=LevelSize_max_y) || (z>=LevelSize_max_z))
            return;
        
        /*Disable Physics Stuff*/
        bulletappstate.getPhysicsSpace().remove(BlockArray[x][y][z].getPhy());    
        BlockArray[x][y][z].setType(-1);
        
        /*Disable Graphics Stuff*/
        blockgeom.DisableBlock(x, y, z);
    }
    
    public Boolean BlockVisible(int x,int y,int z)
    {
        Boolean[] visible = new Boolean[6];  
        Boolean[] check = new Boolean[6];
        int count=0;
        /*
        if(BlockArray[x][y][z].getType()==-1)
            return false;
        */
        for(count=0;count<6;count++)  
        {
            visible[count]=false;
            check[count]=false;
        }
        
        if(x<1)
            check[0]=true;
        if(y<1)
            check[1]=true;
        if(z<1)
            check[2]=true;
        if(x>=(LevelSize_max_x-1))
            check[3]=true;
        if(y>=(LevelSize_max_y-1))
            check[4]=true;
        if(z>=(LevelSize_max_z-1))
            check[5]=true;
        
        if ((check[0]==true) && (check[1]==true) && (check[2]==true) && (check[3]==true) && (check[4]==true) && (check[5]==true))
            return true;
        
        if((x<0) || (y<0) || (z<0) || (x>=LevelSize_max_x) || (y>=LevelSize_max_y) || (z>=LevelSize_max_z))
            return false;
        
        if (BlockArray[x][y][z].getType()==-1)
            return false;

        if (check[0]==false && (BlockArray[x-1][y][z].getType()>=0))
            visible[0] = true;
        if (check[1]==false && (BlockArray[x][y-1][z].getType()>=0))
            visible[1] = true;
        if (check[2]==false && (BlockArray[x][y][z-1].getType()>=0))
            visible[2] = true;
        if (check[3]==false && (BlockArray[x+1][y][z].getType()>=0))
            visible[3] = true;
        if (check[4]==false && (BlockArray[x][y+1][z].getType()>=0))
            visible[4] = true;
        if (check[5]==false && (BlockArray[x][y][z+1].getType()>=0))
            visible[5] = true;         
        
        if ((visible[0]==true) && (visible[1]==true) && (visible[2]==true) && (visible[3]==true) && (visible[4]==true) && (visible[5]==true))
            return false;
        else        
            return true;
    }
    
    public Vector3f RayCastBlock(Vector3f loc, Vector3f dir_, int blocklength, Line line)
    {
        int check_x=0,check_y=0,check_z=0;
        Vector3f pt = new Vector3f();        
        pt.zero();     
        Vector3f dir = new Vector3f();        
        dir.zero();
        Vector3f debug = new Vector3f();        
        dir.zero();
        Vector3f debug2 = new Vector3f();        
        dir.zero();
        
        blocklength*=10;        
        //dir_.normalize();  
        dir_.x*=0.1f;dir_.y*=0.1f;dir_.z*=0.1f;
        //loc.x/=block_size;loc.y/=block_size;loc.z/=block_size;
        
        //Vector3f output = new Vector3f();
        System.out.println("-------------------START------------------");
        for(int i=1;i<(blocklength+1);i++)
        {
            dir.x=dir_.x;dir.y=dir_.y;dir.z=dir_.z;
            dir.x*=i;dir.y*=i;dir.z*=i;
            pt.x=loc.x+dir.x;pt.y=loc.y+dir.y;pt.z=loc.z+dir.z;
            //pt.x+=dir.x;pt.y+=dir.y;pt.z+=dir.z;
            
            //pt.x/=block_size;
            //pt.y/=block_size;
            //pt.z/=block_size;
            
            debug.x=pt.x;
            debug.y=pt.y;
            debug.z=pt.z;
            
            check_x=(int)(pt.x);
            check_y=(int)(pt.y);
            //check_z=(int)(pt.z/=block_size)+1;
            check_z=(int)(pt.z);
            
            //check_x-=2;
            //check_y-=1;
            //check_z-=1;
            
            System.out.println("X: "+check_x+" Y: "+check_y+" Z: "+check_z);
            
            if((check_x>=0) && (check_y>=0) && (check_z>=0) && (check_x<LevelSize_max_x) && (check_y<LevelSize_max_y) && (check_z<LevelSize_max_z))    
            {
                if(BlockArray[check_x][check_y][check_z].getType()!=-1)
                {
                    pt.x=check_x;pt.y=check_y;pt.z=check_z;
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
        System.out.println("-------------------END------------------");
        
        debug2.x=loc.x;
        debug2.y=loc.y+0.1f;
        debug2.z=loc.z;
        
        //debug.x=check_x*block_size;
        //debug.y=check_y*block_size;
        //debug.z=check_z*block_size;
        
        if((pt.x!=-1) && (pt.y!=-1) && (pt.z!=-1))
            line.SetPoints(debug2, debug);
        return pt;        
    }
    
}
