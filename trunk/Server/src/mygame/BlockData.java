/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author crazysaem
 */
public class BlockData {
    
    private int x;
    private int y;
    private int z;
    private byte type;
    
    BlockData(int x, int y, int z, byte type)
    {
        this.x=x;
        this.y=y;
        this.z=z;
        
        this.type=type;
    }
    
    public boolean CheckPos(int x, int y, int z)
    {
        if((this.x==x) && (this.y==y) && (this.z==z))
            return true;
        else
            return false;
    }
    
    public byte GetType()
    {
        return type;
    }
    
    public void SetType(byte type)
    {
        this.type=type;
    }
    
    public int GetXPos()
    {
        return x;
    }
    
    public int GetYPos()
    {
        return y;
    }
    
    public int GetZPos()
    {
        return z;
    }
    
}
