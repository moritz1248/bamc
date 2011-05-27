/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;

/**
 *
 * @author crazysaem
 */
public class Block {

    private int type=0;
    private RigidBodyControl phy;

    public RigidBodyControl getPhy() {
        return phy;
    }
    
    public void AddPhy(Geometry box_geom)
    {
        box_geom.addControl(phy);
    }
    
    Block()
    {
        phy = new RigidBodyControl(0.0f);    
        type = -1;
    }
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
}
