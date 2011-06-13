/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;

/**
 *
 * @author crazysaem
 */
public class HighlightBlock {
    private Geometry geom;
    private AssetManager assetmanager;
    
    HighlightBlock(AssetManager assetmanager_)
    {
        assetmanager=assetmanager_;
        
        Box b = new Box(new Vector3f(0,0,0), 0, 0, 0);
        geom = new Geometry("Box", b);        
    }
    
    public void Highlight(int x, int y, int z, float size, ColorRGBA color)
    {
        Box b = new Box(new Vector3f(x+0.5f,y+0.5f,z+0.5f), 0.5f*size, 0.5f*size, 0.5f*size);
        b.setMode(Mesh.Mode.Lines);
        geom.setMesh(b);        
        Material mat = new Material(assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);   
    }
    
    public Geometry GetGeometry()
    {
        return geom;
    }
    
}
