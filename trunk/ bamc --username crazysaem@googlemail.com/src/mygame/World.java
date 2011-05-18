/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

/**
 *
 * @author crazysaem
 */
public class World {
    private int LevelSize_x,LevelSize_y,LevelSize_z;
    private AssetManager assetmanager;
    private int[][][] BlockArray;
    private int t_max;
    
    public World(AssetManager asset_manager)
    {
        LevelSize_x=59; //Breite
        LevelSize_y=0;  //Höhe
        LevelSize_z=0; //Tiefe
        
        assetmanager=asset_manager;
    }
    
    public void LoadLevel(String level)
    {
        int x,y,z=0;
        Random rand = new Random();
        t_max=4;
        
        LevelSize_x=100; //Breite
        LevelSize_y=100;  //Höhe
        LevelSize_z=100; //Tiefe     
        
        BlockArray = new int[LevelSize_x][LevelSize_y][LevelSize_z];
        
        for(y=0;y<LevelSize_y;y++)
        {
            for(z=0;z<LevelSize_z;z++)
            {
                for(x=0;x<LevelSize_x;x++)
                {
                        BlockArray[x][y][z]=rand.nextInt(4) + 0;
                }                
            }
        }        
    }
    
    private Boolean BlockVisible(int x,int y,int z)
    {
        Boolean[] visible = new Boolean[6];  
        Boolean[] check = new Boolean[6];
        int count=0;
        
        if(BlockArray[x][y][z]==-1)
            return false;
        
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
        if(x>=(LevelSize_x-1))
            check[3]=true;
        if(y>=(LevelSize_y-1))
            check[4]=true;
        if(z>=(LevelSize_z-1))
            check[5]=true;
        
        if ((check[0]==true) && (check[1]==true) && (check[2]==true) && (check[3]==true) && (check[4]==true) && (check[5]==true))
            return true;
        
        if (check[0]==false && (BlockArray[x-1][y][z]>=0))
            visible[0] = true;
        if (check[1]==false && (BlockArray[x][y-1][z]>=0))
            visible[1] = true;
        if (check[2]==false && (BlockArray[x][y][z-1]>=0))
            visible[2] = true;
        if (check[3]==false && (BlockArray[x+1][y][z]>=0))
            visible[3] = true;
        if (check[4]==false && (BlockArray[x][y+1][z]>=0))
            visible[4] = true;
        if (check[5]==false && (BlockArray[x][y][z+1]>=0))
            visible[5] = true;

        
        if ((visible[0]==true) && (visible[1]==true) && (visible[2]==true) && (visible[3]==true) && (visible[4]==true) && (visible[5]==true))
            return false;
        else        
            return true;
    }
    
    public void SetBlock(int x, int y, int z, int type)
    {
        BlockArray[x][y][z]=type;
    }
    
    public Node CreateLevelNode()
    {        
        Node BlocksNode = new Node();
        Mesh m = new Mesh();
        int x,y,z,visible_blocks,c;        
        FloatBuffer vertices;
        FloatBuffer texcoords;
        IntBuffer indexes;
                
        visible_blocks=0;        
        for(y=0;y<LevelSize_y;y++)
        {
            for(z=0;z<LevelSize_z;z++)
            {
                for(x=0;x<LevelSize_x;x++)
                { 
                    if (BlockVisible(x,y,z))
                            visible_blocks++;
                }
            }
        }
        
        //vertices = FloatBuffer.allocate(visible_blocks*12*6);
        //texcoords = FloatBuffer.allocate(visible_blocks*8*6);
        //indexes = IntBuffer.allocate(visible_blocks*6*6);
        
        vertices = BufferUtils.createFloatBuffer(visible_blocks*12*6);
        texcoords = BufferUtils.createFloatBuffer(visible_blocks*8*6);
        indexes = BufferUtils.createIntBuffer(visible_blocks*6*6);
        
        c=0;
        for(y=0;y<LevelSize_y;y++)
        {
            for(z=0;z<LevelSize_z;z++)
            {
                for(x=0;x<LevelSize_x;x++)
                {
                    if(BlockVisible(x,y,z))
                    {
                        /*
                        float[] temp = {0f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 0f, 0f, 1f, 0f,     //FRONT
                                        1f, 0f, 0f, 1f, 0f, -1f, 1f, 1f, -1f, 1f, 1f, 0f,   //RIGHT
                                        0f, 0f, -1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 1f, -1f,   //LEFT
                                        0f, 0f, -1f, 1f, 0f, -1f, 1f, 0f, 0f, 0f, 0f, 0f,   //TOP
                                        0f, 1f, 0f, 1f, 1f, 0f, 1f, 1f, -1f, 0f, 1f, -1f,   //BOTTOM
                                        1f, 0f, -1f, 0f, 0f, -1f, 0f, 1f, -1f, 1f, 1f, -1f  //BACK                                      
                                        }; 
                        */
                        float[] temp1 = {x+0f, y+0f, z+0f,  x+1f, y+0f, z+0f,  x+1f, y+1f, z+0f,  x+0f, y+1f, z+0f,      //FRONT
                                         x+1f, y+0f, z+0f,  x+1f, y+0f, z+-1f, x+1f, y+1f, z+-1f, x+1f, y+1f, z+0f,      //RIGHT
                                         x+0f, y+0f, z+-1f, x+0f, y+0f, z+0f,  x+0f, y+1f, z+0f,  x+0f, y+1f, z+-1f,     //LEFT
                                         x+0f, y+0f, z+-1f, x+1f, y+0f, z+-1f, x+1f, y+0f, z+0f,  x+0f, y+0f, z+0f,      //TOP
                                         x+0f, y+1f, z+0f,  x+1f, y+1f, z+0f,  x+1f, y+1f, z+-1f, x+0f, y+1f, z+-1f,     //BOTTOM
                                         x+1f, y+0f, z+-1f, x+0f, y+0f, z+-1f, x+0f, y+1f, z+-1f, x+1f, y+1f, z+-1f      //BACK                                      
                                        };
                        vertices.put(temp1);
                        
                        int t = BlockArray[x][y][z];
                        float s = (float)t/(float)t_max;
                        float s2 = (float)t/(float)t_max+1f/(float)t_max;
                        float[] temp2 = {s, 0f, s2, 0f, s2, 1f, s, 1f, //FRONT
                                         s, 0f, s2, 0f, s2, 1f, s, 1f, //RIGHT
                                         s, 0f, s2, 0f, s2, 1f, s, 1f, //LEFT
                                         s, 0f, s2, 0f, s2, 1f, s, 1f, //TOP
                                         s, 0f, s2, 0f, s2, 1f, s, 1f, //BOTTOM
                                         s, 0f, s2, 0f, s2, 1f, s, 1f, //BACK
                                         };
                        texcoords.put(temp2);
                        
                        int[] temp3 = {c+3,    c+0,    c+1,    c+1,    c+2,    c+3,         //FRONT
                                       c+3+4,  c+0+4,  c+1+4,  c+1+4,  c+2+4,  c+3+4,       //RIGHT
                                       c+3+8,  c+0+8,  c+1+8,  c+1+8,  c+2+8,  c+3+8,       //LEFT
                                       c+3+12, c+0+12, c+1+12, c+1+12, c+2+12, c+3+12,      //TOP
                                       c+3+16, c+0+16, c+1+16, c+1+16, c+2+16, c+3+16,      //BOTTOM
                                       c+3+20, c+0+20, c+1+20, c+1+20, c+2+20, c+3+20       //BACK
                                       };
                        indexes.put(temp3);                        
                        c+=24;
                    }
                }
            }
        }
        
       
        
        m.setBuffer(Type.Position, 3, vertices);
        m.setBuffer(Type.TexCoord, 2, texcoords);
        m.setBuffer(Type.Index,    1, indexes);
        m.updateBound();        
        
        
        System.out.println(m.getTriangleCount());
        
        Geometry geom = new Geometry("crazysaem", m);
        Material mat = new Material(assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetmanager.loadTexture("Textures/Blocks/block_array.jpg");
        mat.setTexture("ColorMap", tex);

        geom.setMaterial(mat);        
        geom.updateModelBound();
        
        BlocksNode.attachChild(geom);
        
        BlocksNode.scale(10);
        
        return BlocksNode;
    }
    
}
