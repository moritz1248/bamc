/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.message.StreamDataMessage;

/**
 *
 * @author crazysaem
 */
public class bamc_messageListener implements MessageListener<Client> {
    private World world = null;
    private DrawPlayers drawplayers = null;
    
    public bamc_messageListener(World world_, DrawPlayers drawplayers_)
    {
        world = world_;    
        drawplayers = drawplayers_;
    }
    
    @Override
    public void messageReceived(Client source, Message m)
    {
        /*
        byte[] data = ((StreamDataMessage)m).getData();
        String buffer = "";
        
        for(int i=0;i<data.length;i++)
        {
            buffer += (char)data[i];
        }
        
        System.out.println(buffer);*/
        
        byte[] data = ((StreamDataMessage)m).getData();
        
        if (data.length<=0)
            return;           
        
        StreamDataMessage outmessage = new StreamDataMessage();
        String sentstring = (char)0 + "Hello Client";
        outmessage.setData(sentstring.getBytes());
        
        //Message type is located in data[0]
        //Message types: (data[0]==...)
        //0: Test Connection, send back "Hello Client" or sth else
        //1: Player Position has changed
        //2: A Box has been removed/added a.k.a. the type has changed
        
        
        
        byte[] tofloat = new byte[4];
        byte[] toint = new byte[4];
        
        switch(data[0])
        {
            case 0:
                source.send(outmessage);
            break;
                
            case 1:
                if(data.length<0)
                    return;
                
                byte playercount = data[1];
                
                if(data.length!=((playercount*24)+2))
                    return;     
                
                
                
                //We get the Player pos in that order PLAYER-ID, X, Y, Z
                //
                //Please notem that the int is converted to byte in order
                //to send it through the net
                //(size is 4 byte)
                //
                //Please also note, that the float is converted to byte in order
                //to send it throught the net
                //e.g 2.7182817 will become [64, 45, -8, 84]
                // (That makes a total of 3*4 bytes)
                
                Vector3f newpos = new Vector3f();
                Vector3f dir = new Vector3f();
                
                Vector3f[] positions = new Vector3f[playercount];
                Vector3f[] directions = new Vector3f[playercount];
                
                for(byte i=0;i<playercount;i++)
                {  
                    //X:
                    tofloat[0] = data[2+i*24];
                    tofloat[1] = data[3+i*24];
                    tofloat[2] = data[4+i*24];
                    tofloat[3] = data[5+i*24];

                    newpos.x = ByteFloatConvert.byteArrayToFloat(tofloat);

                    //Y:
                    tofloat[0] = data[6+i*24];
                    tofloat[1] = data[7+i*24];
                    tofloat[2] = data[8+i*24];
                    tofloat[3] = data[9+i*24];

                    newpos.y = ByteFloatConvert.byteArrayToFloat(tofloat);

                    //Z:
                    tofloat[0] = data[10+i*24];
                    tofloat[1] = data[11+i*24];
                    tofloat[2] = data[12+i*24];
                    tofloat[3] = data[13+i*24];

                    newpos.z = ByteFloatConvert.byteArrayToFloat(tofloat);
                    
                    positions[i] = new Vector3f(newpos.x, newpos.y, newpos.z);
                    
                    //X:
                    tofloat[0] = data[14+i*24];
                    tofloat[1] = data[15+i*24];
                    tofloat[2] = data[16+i*24];
                    tofloat[3] = data[17+i*24];

                    dir.x = ByteFloatConvert.byteArrayToFloat(tofloat);

                    //Y:
                    tofloat[0] = data[18+i*24];
                    tofloat[1] = data[19+i*24];
                    tofloat[2] = data[20+i*24];
                    tofloat[3] = data[21+i*24];

                    dir.y = ByteFloatConvert.byteArrayToFloat(tofloat);

                    //Z:
                    tofloat[0] = data[22+i*24];
                    tofloat[1] = data[23+i*24];
                    tofloat[2] = data[24+i*24];
                    tofloat[3] = data[25+i*24];

                    dir.z = ByteFloatConvert.byteArrayToFloat(tofloat);
                    
                    directions[i] = new Vector3f(dir.x, dir.y, dir.z);
            
                }
                
                drawplayers.setpos(positions);
                drawplayers.setdir(directions);
                
            break;
                
            case 2:                
                if(data.length!=14)
                    return;
                
                //We will get the Data in that Order:
                //X, Y, Z, BLOCK-TYPE
                //X and Y and Z are int type, see above in case 1
                
                toint[0] = data[1];
                toint[1] = data[2];
                toint[2] = data[3];
                toint[3] = data[4];
                
                int x = ByteIntConvert.byteArrayToInt(toint);
                
                toint[0] = data[5];
                toint[1] = data[6];
                toint[2] = data[7];
                toint[3] = data[8];
                
                int y = ByteIntConvert.byteArrayToInt(toint);
                
                toint[0] = data[9];
                toint[1] = data[10];
                toint[2] = data[11];
                toint[3] = data[12];
                
                int z = ByteIntConvert.byteArrayToInt(toint);
                
                world.SetBlockType(x, y, z, data[13]);
                world.GetBlockGeom().ForecUpdate();
            break;
                
            case 3:
                if(data.length<4)
                    return;
                
                byte size[] = new byte[4];
                size[0] = data[1];
                size[1] = data[2];
                size[2] = data[3];
                size[3] = data[4];
                
                int isize = ByteIntConvert.byteArrayToInt(size);
                
                if(data.length!=((isize*13)+4+1))
                    return;  
                
                byte x_[] = new byte[4];
                byte y_[] = new byte[4];
                byte z_[] = new byte[4];
                byte t;
                int ix;
                int iy;
                int iz;
                
                for(int i=0;i<isize;i++)
                {
                    x_[0] = data[5+i*13];
                    x_[1] = data[6+i*13];
                    x_[2] = data[7+i*13];
                    x_[3] = data[8+i*13];
                    
                    y_[0] = data[9+i*13];
                    y_[1] = data[10+i*13];
                    y_[2] = data[11+i*13];
                    y_[3] = data[12+i*13];
                    
                    z_[0] = data[13+i*13];
                    z_[1] = data[14+i*13];
                    z_[2] = data[15+i*13];
                    z_[3] = data[16+i*13];
                    
                    t = data[17+i*13];
                    
                    ix = ByteIntConvert.byteArrayToInt(x_);
                    iy = ByteIntConvert.byteArrayToInt(y_);
                    iz = ByteIntConvert.byteArrayToInt(z_);
                    
                    world.SetBlockType(ix, iy, iz, t);
                }
                
                world.GetBlockGeom().ForecUpdate();
                
            break;
                
            default:
                System.out.println("ERROR, message was not recognized! Message is:");
                String buffer = "";        
                for(int i=0;i<data.length;i++)
                    buffer += (char)data[i];
                System.out.println("\""+buffer+"\"");
            break;
        }
    }
    
}
