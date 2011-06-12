/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import com.jme3.network.MessageConnection;
import com.jme3.network.MessageListener;
import com.jme3.network.base.DefaultServer;
import com.jme3.network.message.StreamDataMessage;

/**
 *
 * @author crazysaem
 */
public class bamc_messageListener implements MessageListener<MessageConnection> {
    GameData gamedata = null;
    DefaultServer server = null;
    
    public bamc_messageListener(GameData gamedata_, DefaultServer server_)
    {
        gamedata = gamedata_;
        server = server_;
    }
    
    @Override
    public void messageReceived(MessageConnection source, Message m)
    {
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
                if(data.length!=29)
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
                
                toint[0] = data[1];
                toint[1] = data[2];
                toint[2] = data[3];
                toint[3] = data[4];
                
                int id = ByteIntConvert.byteArrayToInt(toint);
                
                Vector3f newpos = new Vector3f();
                Vector3f dir = new Vector3f();
                
                //X:
                tofloat[0] = data[5];
                tofloat[1] = data[6];
                tofloat[2] = data[7];
                tofloat[3] = data[8];
                
                newpos.x = ByteFloatConvert.byteArrayToFloat(tofloat);
                
                //Y:
                tofloat[0] = data[9];
                tofloat[1] = data[10];
                tofloat[2] = data[11];
                tofloat[3] = data[12];
                
                newpos.y = ByteFloatConvert.byteArrayToFloat(tofloat);
                
                //Z:
                tofloat[0] = data[13];
                tofloat[1] = data[14];
                tofloat[2] = data[15];
                tofloat[3] = data[16];
                
                newpos.z = ByteFloatConvert.byteArrayToFloat(tofloat);
                
                //X:
                tofloat[0] = data[17];
                tofloat[1] = data[18];
                tofloat[2] = data[19];
                tofloat[3] = data[20];
                
                dir.x = ByteFloatConvert.byteArrayToFloat(tofloat);
                
                //Y:
                tofloat[0] = data[21];
                tofloat[1] = data[22];
                tofloat[2] = data[23];
                tofloat[3] = data[24];
                
                dir.y = ByteFloatConvert.byteArrayToFloat(tofloat);
                
                //Z:
                tofloat[0] = data[25];
                tofloat[1] = data[26];
                tofloat[2] = data[27];
                tofloat[3] = data[28];
                
                dir.z = ByteFloatConvert.byteArrayToFloat(tofloat);
                
                gamedata.setplayerpos(newpos, id);
                gamedata.setplayerdir(dir, id);
                
                //outmessage.setData(data);
                //server.broadcast(outmessage);
                
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
                
                gamedata.SetBlockType(x, y, z, data[13]);

                outmessage = new StreamDataMessage();
                outmessage.setData(data);
                server.broadcast(outmessage);
                
                System.out.println("Block on Position "+x+","+y+","+z+" has been set to "+data[13]);
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
