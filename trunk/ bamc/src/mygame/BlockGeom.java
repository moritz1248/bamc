/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author crazysaem
 */
public class BlockGeom {
    Geometry geom;
    BulletAppState bulletappstate;
    private int LevelSize_max_x,LevelSize_max_y,LevelSize_max_z;
    private int t_max=4;
    private AssetManager assetmanager;
    
    BlockGeom(int x_max, int y_max, int z_max, AssetManager assetmanager_)
    {
        LevelSize_max_x=x_max;  //Breite
        LevelSize_max_y=y_max;  //Höhe
        LevelSize_max_z=z_max;  //Tiefe   
        int space=LevelSize_max_x*LevelSize_max_y*LevelSize_max_z;
        Mesh m = new Mesh();        
        assetmanager=assetmanager_;
        
        FloatBuffer vertices = BufferUtils.createFloatBuffer(space*12*6);
        FloatBuffer texcoords = BufferUtils.createFloatBuffer(space*8*6);
        IntBuffer indexes = BufferUtils.createIntBuffer(space*6*6);
        
        m.setBuffer(Type.Position, 3, vertices);
        m.setBuffer(Type.TexCoord, 2, texcoords);
        m.setBuffer(Type.Index,    1, indexes);
        m.updateBound();
        
        geom = new Geometry("crazysaem", m);        
        Material mat = new Material(assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetmanager.loadTexture("Textures/Blocks/block_array.jpg");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);         
        geom.scale(2.5f);       
        geom.updateModelBound();
    }
    
    public Geometry GetGeom()
    {
        return geom;
    }        
            
    public void EnableBlock(int x, int y, int z, int type)
    {                
        FloatBuffer vertices  = geom.getMesh().getFloatBuffer(Type.Position);
        FloatBuffer texcoords = geom.getMesh().getFloatBuffer(Type.TexCoord);
        IndexBuffer indexes   = geom.getMesh().getIndexBuffer();
        
        int allg_pos=x+y*LevelSize_max_x+z*LevelSize_max_x*LevelSize_max_y;
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
        
        geom.getMesh().updateBound();
        geom.updateModelBound();
    }
    
    public void DisableBlock(int x, int y, int z)
    {
        FloatBuffer vertices  = geom.getMesh().getFloatBuffer(Type.Position);
        FloatBuffer texcoords = geom.getMesh().getFloatBuffer(Type.TexCoord);
        IndexBuffer indexes   = geom.getMesh().getIndexBuffer();  
        
        int allg_pos=x+y*LevelSize_max_x+z*LevelSize_max_x*LevelSize_max_y;
        int ver_pos=allg_pos*12*6;
        int tex_pos=allg_pos*8*6;
        int ind_pos=allg_pos*6*6;
        int i=0;
               
        for(i=0;i<(12*6);i++)
            vertices.put(ver_pos+i, 0);
             
        for(i=0;i<(8*6);i++)
            texcoords.put(tex_pos+i, 0);
               
        for(i=0;i<(6*6);i++)
            indexes.put(ind_pos+i, 0);
        geom.getMesh().updateBound();
        geom.updateModelBound();
    }
    
     private float[] BlockPos(int x, int y, int z)
    {
        float[] temp1 = {x+0f, y+0f, z+0f,  x+1f, y+0f, z+0f,  x+1f, y+1f, z+0f,  x+0f, y+1f, z+0f,      //FRONT
                         x+1f, y+0f, z+0f,  x+1f, y+0f, z+-1f, x+1f, y+1f, z+-1f, x+1f, y+1f, z+0f,      //RIGHT
                         x+0f, y+0f, z+-1f, x+0f, y+0f, z+0f,  x+0f, y+1f, z+0f,  x+0f, y+1f, z+-1f,     //LEFT
                         x+0f, y+0f, z+-1f, x+1f, y+0f, z+-1f, x+1f, y+0f, z+0f,  x+0f, y+0f, z+0f,      //TOP
                         x+0f, y+1f, z+0f,  x+1f, y+1f, z+0f,  x+1f, y+1f, z+-1f, x+0f, y+1f, z+-1f,     //BOTTOM
                         x+1f, y+0f, z+-1f, x+0f, y+0f, z+-1f, x+0f, y+1f, z+-1f, x+1f, y+1f, z+-1f      //BACK                                      
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
        int[] temp3 = {c+3,    c+0,    c+1,    c+1,    c+2,    c+3,                         //FRONT
                       c+3+4,  c+0+4,  c+1+4,  c+1+4,  c+2+4,  c+3+4,       //RIGHT
                       c+3+8,  c+0+8,  c+1+8,  c+1+8,  c+2+8,  c+3+8,       //LEFT
                       c+3+12, c+0+12, c+1+12, c+1+12, c+2+12, c+3+12,      //TOP
                       c+3+16, c+0+16, c+1+16, c+1+16, c+2+16, c+3+16,      //BOTTOM
                       c+3+20, c+0+20, c+1+20, c+1+20, c+2+20, c+3+20       //BACK
                       };
        return temp3;
    }
    
}
