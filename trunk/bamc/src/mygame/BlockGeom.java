/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crazysaem
 */
public class BlockGeom implements Callable {
    Geometry geom;
    //BulletAppState bulletappstate;
    private int LevelSize_max_x,LevelSize_max_y,LevelSize_max_z;
    private int t_max=4;
    private AssetManager assetmanager;
    private int space;
    private int bufferpos;
    private byte[][][] blockarray;
    private Vector3f rendercenter;
    private boolean update=false;
    
    BlockGeom(int x_geom, int y_geom, int z_geom, int x_max, int y_max, int z_max, AssetManager assetmanager_)
    {
        LevelSize_max_x=x_max;  //Breite
        LevelSize_max_y=y_max;  //Höhe
        LevelSize_max_z=z_max;  //Tiefe   
        
        space=x_geom*y_geom*z_geom;
        Mesh m = new Mesh();        
        assetmanager=assetmanager_;
        bufferpos=0;
        int x,y,z;
        Random rand = new Random();
        
        rendercenter = new Vector3f(0,0,0);
        
        FloatBuffer vertices = BufferUtils.createFloatBuffer(space*12*6);
        FloatBuffer texcoords = BufferUtils.createFloatBuffer(space*8*6);
        IntBuffer indexes = BufferUtils.createIntBuffer(space*6*6);
        
        int i=0;
     
        for(i=0;i<(space*12*6);i++)
            vertices.put(i, 0);
         
        for(i=0;i<(space*8*6);i++)
            texcoords.put(i, 0);
               
        for(i=0;i<(space*6*6);i++)
            indexes.put(i, 0);
        
        m.setBuffer(Type.Position, 3, vertices);
        m.setBuffer(Type.TexCoord, 2, texcoords);
        m.setBuffer(Type.Index,    1, indexes);
        //m.updateBound();
        
        geom = new Geometry("crazysaem", m);        
        Material mat = new Material(assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetmanager.loadTexture("Textures/Blocks/block_array.jpg");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);      
        
        blockarray = new byte[LevelSize_max_x][LevelSize_max_y][LevelSize_max_z]; 
        
        
        for(x=0;x<LevelSize_max_x;x++)
            for(y=0;y<LevelSize_max_y;y++)
                for(z=0;z<LevelSize_max_z;z++)
                    blockarray[x][y][z] = -1;
        
        
        for(x=0;x<LevelSize_max_x;x++)
            for(y=0;y<LevelSize_max_y;y++)
                for(z=0;z<LevelSize_max_z;z++)
                    blockarray[x][y][z] = (byte)(rand.nextInt(4) + 0);
      
        //geom.updateModelBound();
    }
    
    @Override
    public Object call() 
    {
        //if(update==false)
        //    return null;
        
        update=false;
        
        //int radius_pos=1;
        
        Reset();

        DrawBlocks((int)rendercenter.x,(int)rendercenter.y,(int)rendercenter.z,30);

        Update();    
        
        System.out.println("Used "+bufferpos+" of "+space+" avaivable space");
        
        return null;        
    }
    
    public boolean UpdateNeeded()
    {
        return update;
    }
    
    public void ForecUpdate()
    {
        update=true;
    }
    
    public void SetPlayerPos(Vector3f p)
    {
        if((Math.abs(rendercenter.x-p.x)>=10) || (Math.abs(rendercenter.y-p.y)>=10) || (Math.abs(rendercenter.z-p.z)>=10))
        {    
            rendercenter.x=p.x;       
            rendercenter.y=p.y;
            rendercenter.z=p.z;
            
            update=true;
        }
    }    
    
    public byte GetBlockType(int x, int y, int z)
    {
        return blockarray[x][y][z];
    }
    
    public void SetBlockType(int x, int y, int z, byte type)
    {
        if(x<1)
            return;
        if(y<1)
            return;
        if(z<1)
            return;
        if(x>=(LevelSize_max_x))
            return;
        if(y>=(LevelSize_max_y))
            return;
        if(z>=(LevelSize_max_z))
            return;
        
        blockarray[x][y][z] = type;
    }
    
    private void DrawBlocks(int p_x, int p_y, int p_z, int radius)
    {
        int x,y,z;
        
        for(x=-radius;x<=radius;x++)
        {
            for(y=-radius;y<=radius;y++)
            {
                for(z=-radius;z<=radius;z++)
                {
                    if(BlockVisible(x+p_x,y+p_y,z+p_z))
                    {
                        EnableBlock(x+p_x,y+p_y,z+p_z);
                        if(bufferpos>space)                                    
                            return;
                    }
                    
                }
            }
        }
    
    }
    
    /*
    private boolean EnableBlocksatRadius(int p_x, int p_y, int p_z, int pos)
    {
        //int blockcount=0;
        
        int a=pos;
        
        //for(int a=2;2<30;a++)
        //{
            for(int x=-a;x<=a;x++)
            {
                for(int y=-a;y<=a;y++)
                {
                    for(int z=-a;z<=a;z++)
                    {
                        if( !( (Math.abs(x)<=(a-1)) && (Math.abs(y)<=(a-1)) && (Math.abs(z)<=(a-1)) ) )
                        {
                            if(BlockVisible(x+p_x,y+p_y,z+p_z))
                            {
                                EnableBlock(x+p_x,y+p_y,z+p_z);
                                if(bufferpos>space)                                    
                                    return false;
                            }                                                                                    
                        }       
                    }
                }                
            }
            
            return true;
        //}
    }*/
    
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
        
        if (blockarray[x][y][z]==-1)
            return false;

        if (check[0]==false && (blockarray[x-1][y][z]>=0))
            visible[0] = true;
        if (check[1]==false && (blockarray[x][y-1][z]>=0))
            visible[1] = true;
        if (check[2]==false && (blockarray[x][y][z-1]>=0))
            visible[2] = true;
        if (check[3]==false && (blockarray[x+1][y][z]>=0))
            visible[3] = true;
        if (check[4]==false && (blockarray[x][y+1][z]>=0))
            visible[4] = true;
        if (check[5]==false && (blockarray[x][y][z+1]>=0))
            visible[5] = true;         
        
        if ((visible[0]==true) && (visible[1]==true) && (visible[2]==true) && (visible[3]==true) && (visible[4]==true) && (visible[5]==true))
            return false;
        else        
            return true;
    }
    
    public Geometry GetGeom()
    {
        return geom;
    }    
    
    private void Update()
    {
        geom.getMesh().getBuffer(Type.Position).setUpdateNeeded();
        geom.getMesh().getBuffer(Type.TexCoord).setUpdateNeeded();
        geom.getMesh().getBuffer(Type.Index).setUpdateNeeded();
        
        geom.getMesh().updateBound();
        //geom.getMesh().updateCounts();    
        geom.updateModelBound();
    }
    
    private void Reset()
    {
        FloatBuffer vertices  = geom.getMesh().getFloatBuffer(Type.Position);
        FloatBuffer texcoords = geom.getMesh().getFloatBuffer(Type.TexCoord);
        IndexBuffer indexes   = geom.getMesh().getIndexBuffer();
        
        int i=0;
     
        for(i=0;i<(space*12*6);i++)
            vertices.put(i, 0);
         
        for(i=0;i<(space*8*6);i++)
            texcoords.put(i, 0);
               
        for(i=0;i<(space*6*6);i++)
            indexes.put(i, 0);
        
        bufferpos=0;
    }
            
    private void EnableBlock(int x, int y, int z)
    {
        //blockarray[x][y][z]=type;
        int type=blockarray[x][y][z];
                
        
        FloatBuffer vertices  = geom.getMesh().getFloatBuffer(Type.Position);
        FloatBuffer texcoords = geom.getMesh().getFloatBuffer(Type.TexCoord);
        IndexBuffer indexes   = geom.getMesh().getIndexBuffer();
        
        if(bufferpos>space)
            return;
        
        int allg_pos=bufferpos;
        bufferpos++;
        int ver_pos=allg_pos*12*6;
        int tex_pos=allg_pos*8*6;
        int ind_pos=allg_pos*6*6;
        int i=0;
        
        float[] ver_data = BlockPos(x,y,z);        
        for(i=0;i<(12*6);i++)
            vertices.put(ver_pos+i, ver_data[i]);
        
        float[] tex_data = BlockTexUV(type);        
        for(i=0;i<(8*6);i++)
            texcoords.put(tex_pos+i, tex_data[i]);
        
        int[] ind_data = BlockIndexes(allg_pos*24);        
        for(i=0;i<(6*6);i++)
            indexes.put(ind_pos+i, ind_data[i]);
        

    }
    /*
    private void DisableBlock(int x, int y, int z)
    {
        //blockarray[x][y][z]=-1;
        
        FloatBuffer vertices  = geom.getMesh().getFloatBuffer(Type.Position);
        
        int allg_pos=x+y*LevelSize_max_x+z*LevelSize_max_x*LevelSize_max_y;
        int ver_pos=allg_pos*12*6;
        int i=0;
               
        for(i=0;i<(12*6);i++)
            vertices.put(ver_pos+i, 0);
        

    }
    */    
     private float[] BlockPos(int x, int y, int z)
    {
        /*
        float[] temp1 = {x+0f, y+0f, z+0f,  x+1f, y+0f, z+0f,  x+1f, y+1f, z+0f,  x+0f, y+1f, z+0f,      //FRONT
                         x+1f, y+0f, z+0f,  x+1f, y+0f, z+-1f, x+1f, y+1f, z+-1f, x+1f, y+1f, z+0f,      //RIGHT
                         x+0f, y+0f, z+-1f, x+0f, y+0f, z+0f,  x+0f, y+1f, z+0f,  x+0f, y+1f, z+-1f,     //LEFT
                         x+0f, y+0f, z+-1f, x+1f, y+0f, z+-1f, x+1f, y+0f, z+0f,  x+0f, y+0f, z+0f,      //TOP
                         x+0f, y+1f, z+0f,  x+1f, y+1f, z+0f,  x+1f, y+1f, z+-1f, x+0f, y+1f, z+-1f,     //BOTTOM
                         x+1f, y+0f, z+-1f, x+0f, y+0f, z+-1f, x+0f, y+1f, z+-1f, x+1f, y+1f, z+-1f      //BACK                                      
                         };
        */
        float[] temp1 = {x+0f, y+0f, z+0f, x+1f, y+0f, z+0f, x+1f, y+1f, z+0f, x+0f, y+1f, z+0f,      //FRONT
                         x+1f, y+0f, z+0f, x+1f, y+0f, z+1f, x+1f, y+1f, z+1f, x+1f, y+1f, z+0f,      //RIGHT
                         x+0f, y+0f, z+1f, x+0f, y+0f, z+0f, x+0f, y+1f, z+0f, x+0f, y+1f, z+1f,     //LEFT
                         x+0f, y+0f, z+1f, x+1f, y+0f, z+1f, x+1f, y+0f, z+0f, x+0f, y+0f, z+0f,      //TOP
                         x+0f, y+1f, z+0f, x+1f, y+1f, z+0f, x+1f, y+1f, z+1f, x+0f, y+1f, z+1f,     //BOTTOM
                         x+1f, y+0f, z+1f, x+0f, y+0f, z+1f, x+0f, y+1f, z+1f, x+1f, y+1f, z+1f      //BACK                                      
                         };
        return temp1;
    }
    
    private float[] BlockTexUV(int type)
    {
        int t = type;
        float s = (float)t/(float)t_max;
        float s2 = (float)t/(float)t_max+1f/(float)t_max;
        float[] temp2 = {s, 0f, s2, 0f, s2, 1f, s, 1f, //FRONT
                         s, 0f, s2, 0f, s2, 1f, s, 1f, //RIGHT
                         s, 0f, s2, 0f, s2, 1f, s, 1f, //LEFT
                         s, 0f, s2, 0f, s2, 1f, s, 1f, //TOP
                         s, 0f, s2, 0f, s2, 1f, s, 1f, //BOTTOM
                         s, 0f, s2, 0f, s2, 1f, s, 1f, //BACK
                         };
        return temp2;
    }
    
    private int[] BlockIndexes(int c)
    {
        /*
        int[] temp3 = {c+3,    c+0,    c+1,    c+1,    c+2,    c+3,                         //FRONT
                       c+3+4,  c+0+4,  c+1+4,  c+1+4,  c+2+4,  c+3+4,       //RIGHT
                       c+3+8,  c+0+8,  c+1+8,  c+1+8,  c+2+8,  c+3+8,       //LEFT
                       c+3+12, c+0+12, c+1+12, c+1+12, c+2+12, c+3+12,      //TOP
                       c+3+16, c+0+16, c+1+16, c+1+16, c+2+16, c+3+16,      //BOTTOM
                       c+3+20, c+0+20, c+1+20, c+1+20, c+2+20, c+3+20       //BACK
                       };
        */
        int[] temp3 = {c+1,    c+0,    c+3,    c+3,    c+2,    c+1,                         //FRONT
                       c+1+4,  c+0+4,  c+3+4,  c+3+4,  c+2+4,  c+1+4,       //RIGHT
                       c+1+8,  c+0+8,  c+3+8,  c+3+8,  c+2+8,  c+1+8,       //LEFT
                       c+1+12, c+0+12, c+3+12, c+3+12, c+2+12, c+1+12,      //TOP
                       c+1+16, c+0+16, c+3+16, c+3+16, c+2+16, c+1+16,      //BOTTOM
                       c+1+20, c+0+20, c+3+20, c+3+20, c+2+20, c+1+20       //BACK
                       };
        return temp3;
    }
    
    
    
}
