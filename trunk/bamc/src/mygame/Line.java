/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import java.nio.FloatBuffer;

/**
 *
 * @author crazysaem
 */
public class Line {
    
    //private Mesh mesh = new Mesh();
    //private AssetManager assetmanager;
    private Geometry geom;
    private AssetManager assetmanager;
    
    Line(AssetManager assetmanager_)
    {
        assetmanager=assetmanager_;
                
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.Lines);
        mesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{ 0, 0, 0, 0, 0, 0});
        mesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{ 0, 1 });
        
        //Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        geom = new Geometry("Box", mesh);
        Material mat = new Material(assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.randomColor());
        geom.setMaterial(mat);
        
        geom.getMesh().getBuffer(Type.Position).setUpdateNeeded();
        //geom.getMesh().getBuffer(Type.TexCoord).setUpdateNeeded();
        geom.getMesh().getBuffer(Type.Index).setUpdateNeeded();
        
        geom.getMesh().updateBound();
        geom.getMesh().updateCounts();    
        geom.updateModelBound();
    }
    
    public void SetPoints(Vector3f start, Vector3f end)
    {
        FloatBuffer vertices  = geom.getMesh().getFloatBuffer(Type.Position);
        
        vertices.put(0, start.x);
        vertices.put(1, start.y);
        vertices.put(2, start.z);
        
        vertices.put(3, end.x);
        vertices.put(4, end.y);
        vertices.put(5, end.z);
        
        geom.getMesh().getBuffer(Type.Position).setUpdateNeeded();
        //geom.getMesh().getBuffer(Type.TexCoord).setUpdateNeeded();
        //geom.getMesh().getBuffer(Type.Index).setUpdateNeeded();
        
        geom.getMesh().updateBound();
        geom.getMesh().updateCounts();    
        geom.updateModelBound();
    }
    
    public Geometry GetGeom()
    {
        return geom;
    }
    
}
