/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author crazysaem
 */
public class Player {
    private Vector3f pos;
    private World world;
    private Vector3f jump;
    private Vector3f gravity;
    private float jumpspeed;
    private float jumpheight;
    private float gravspeed;
    private boolean falling;
    
    Player(World world_)
    {
        world=world_;
        pos = new Vector3f();
        pos.zero();
        jump = new Vector3f();
        jump.zero();
        gravity = new Vector3f();
        gravity.zero();
        jumpspeed=0;
        jumpheight=0;
        
        falling=false;
    }
    
    public boolean isFalling()
    {
        return falling;
    }
    
    public void SetStartPos(Vector3f startpos)
    {
        pos.x=startpos.x;
        pos.y=startpos.y;
        pos.z=startpos.z;
    }
    
    public void SetGravity(Vector3f gravdir, float speed)
    {
        gravity.x=gravdir.x;
        gravity.y=gravdir.y;
        gravity.z=gravdir.z;
        
        gravspeed=speed;
    }
    
    public void SetJump(Vector3f jumpdir, float speed, float height)
    {
        falling=true;
                
        jump.x=jumpdir.x;
        jump.y=jumpdir.y;
        jump.z=jumpdir.z;
        
        jump = jump.normalize();
        
        jumpspeed=speed;
        
        jumpheight=height;
    }
    
    public void Update(float tpf)
    {
        /*
        Vector3f newdir = new Vector3f(-gravity.x+jump.x*jumpspeed, -gravity.y+jump.y*jumpspeed, -gravity.z+jump.z*jumpspeed);
        newdir.x*=tpf;
        newdir.y*=tpf;
        newdir.z*=tpf;
        jumpheight-=jump.length()*jumpspeed*tpf;
        */
        
        Vector3f gravdir = new Vector3f(-gravity.x*gravspeed,-gravity.y*gravspeed,-gravity.z*gravspeed);
        Vector3f jumpdir = new Vector3f(jump.x*jumpspeed,jump.y*jumpspeed,jump.z*jumpspeed);
        
        if(jumpdir.length()>0)
        {        
            jumpdir.x*=tpf;
            jumpdir.y*=tpf;
            jumpdir.z*=tpf;
            jumpheight-=jump.length()*jumpspeed*tpf;
            
            if(jumpheight<=0)
            {
                jumpheight=0;

                jump.x=0;            
                jump.y=0;            
                jump.z=0;

                jumpspeed=0;
            }
            
            if(tpf>1)
                return;
            
            MoveTo(jumpdir);   
        }
        else
        {
            gravdir.x*=tpf;
            gravdir.y*=tpf;
            gravdir.z*=tpf;
            
            if(tpf>1)
                return;
            
            falling=MoveTo(gravdir);  
        }      
    }  
    
    public boolean BlockExists(int x, int y, int z)
    {        
        if(x<0)
            return false;
        if(y<0)
            return false;
        if(z<0)
            return false;
        if(x>=(world.GetXMax()))
            return false;
        if(y>=(world.GetYMax()))
            return false;
        if(z>=(world.GetZMax()))
            return false;  
        
        if(world.GetBlockType(x, y, z)>=0)
            return true;
        else
            return false;
    }
    
    public boolean MoveTo(Vector3f dir)
    {
        Vector3f distance = new Vector3f(0.2f, 0.2f, 0.2f);
        if(dir.x<0) distance.x*=-1;
        if(dir.y<0) distance.y*=-1;
        if(dir.z<0) distance.z*=-1;
        
                
        Vector3f checkpos = new Vector3f(pos.x+dir.x+distance.x, pos.y+dir.y+distance.y, pos.z+dir.z+distance.z);
        Vector3f newpos = new Vector3f(pos.x+dir.x, pos.y+dir.y, pos.z+dir.z);
        //if(!world.BlockVisible((int)checkpos.x, (int)checkpos.y, (int)checkpos.z))
        
        //System.out.println(pos);
        
        if((!BlockExists((int)checkpos.x, (int)checkpos.y, (int)checkpos.z)) && (!BlockExists((int)checkpos.x, (int)checkpos.y+1, (int)checkpos.z)))
        {
            pos.x=newpos.x;
            pos.y=newpos.y;
            pos.z=newpos.z;
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Vector3f GetPos()
    {
        return pos;
    }
    
}
