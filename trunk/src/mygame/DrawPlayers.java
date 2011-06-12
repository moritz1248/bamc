/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Node;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author crazysaem
 */
public class DrawPlayers {
    private Spatial[] gordons = null;
    private AnimChannel[] gordons_animation = null;
    private int count=0;
    private int c=0;
    private Vector3f[] newpos = null;
    private Vector3f[] oldpos = null;
    private Vector3f[] dir = null;
    Node node = new Node();
    Vector3f debug = new Vector3f(0,0,0);
    Player player = null;
    private float tpfcount = 0;
    private boolean walk = false;
    
    public DrawPlayers(int count_, AssetManager assetmanager, Player player_)
    {
        player = player_;
        count=count_;
        gordons = new Spatial[count];
        gordons_animation = new AnimChannel[count];
        for(int i=0;i<count;i++)
        {
            gordons[i] = assetmanager.loadModel("Models/ogre/boxMesh.mesh.j3o");
            gordons[i].setLocalScale(0.068f);  
            //gordons[i].rotate(-1.57f, 0, 0);
            
            AnimControl control = gordons[i].getControl(AnimControl.class);
            
            gordons_animation[i] = control.createChannel();
            gordons_animation[i].setAnim("still");
            
            /*
            Box b = new Box(Vector3f.ZERO, 1, 1, 1);
            geom[i] = new Geometry("Box"+i, b);
            Material mat = new Material(assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Red);
            geom[i].setMaterial(mat); 
             */
            

        }
    }
    
    public void setpos(Vector3f[] pos)
    {
        newpos = pos;
    }
    
    public void setdir(Vector3f[] dir)
    {
        this.dir = dir;
    }
    
    public void update(float tpf)
    {
        if(walk==true)
            tpfcount+=tpf;
        
        if(tpfcount>0.3)
        {
            tpfcount=0;
            walk=false;
        }
                
        if((newpos==null) || (dir==null))
            return;
        
        if(newpos.length!=dir.length)
            return;
        
        c=newpos.length;
        if (c>count)
            c=count;
        
        for(int a=0;a<count;a++)
            gordons[a].setLocalTranslation(new Vector3f(-5000,-5000,-5000));
        
        for(int i=0;i<c;i++)
        {
            if((newpos!=null) && (oldpos!=null))
                if(newpos.length==oldpos.length)
                {
                    if((newpos[i].x==oldpos[i].x) && (newpos[i].y==oldpos[i].y) && (newpos[i].z==oldpos[i].z))
                    {
                        if(!gordons_animation[i].getAnimationName().contains("stand") && (walk==false))
                        {                                
                            gordons_animation[i].setAnim("stand");
                            gordons_animation[i].setSpeed(0.5f);
                        }
                    }
                    else
                    {
                        if(Math.abs(newpos[i].y-oldpos[i].y)>=0.1)
                        {
                            gordons_animation[i].setAnim("jump");
                            walk=false;
                        }
                        else
                        {
                            walk=true;
                            if(!gordons_animation[i].getAnimationName().contains("walk"))
                            {
                                gordons_animation[i].setAnim("walk");
                                gordons_animation[i].setSpeed(1.3f);
                            }
                        }
                    }
                }
            
            if(!(
              (Math.abs(player.GetPos().x-newpos[i].x)<=0.6) &&
              (Math.abs(player.GetPos().y-newpos[i].y)<=0.6) &&
              (Math.abs(player.GetPos().z-newpos[i].z)<=0.6)))
               
            gordons[i].setLocalTranslation(newpos[i].x, newpos[i].y, newpos[i].z);
            
            Vector3f bdir = dir[i].clone();
            bdir.y=0;
            bdir = bdir.normalize();
            
            Vector3f look = new Vector3f(newpos[i].x+bdir.x, newpos[i].y+bdir.y, newpos[i].z+bdir.z);
            
            Vector3f up = new Vector3f(0,1,0);
            up = up.normalize();
            
            gordons[i].lookAt(look, up);
        }
        
        oldpos = newpos.clone();
                
        //node.detachAllChildren();        
    }
    
    public Node GetNode()
    {   
        for(int i=0;i<count;i++)
            node.attachChild(gordons[i]);
        
        return node;        
    }
}
