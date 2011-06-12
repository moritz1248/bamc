/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import com.jme3.network.message.StreamDataMessage;
import com.jme3.network.message.ZIPCompressedMessage;

/**
 *
 * @author crazysaem
 */
public class bamc_connectionlistener implements ConnectionListener {    
    private GameData gamedata = null;
    
    public bamc_connectionlistener(GameData gamedata_)
    {
        gamedata = gamedata_;
    }
    
    @Override
    public void connectionAdded(Server server, HostedConnection conn)
    {
        System.out.println("Connection has been added");             
        gamedata.addplayer(conn.getId());     
        
        StreamDataMessage outmessage = new StreamDataMessage();
        
        outmessage.setData(gamedata.GetBlockMessage((byte)3));
        
        conn.send(outmessage);
    }
    
    @Override
    public void connectionRemoved(Server server, HostedConnection conn) 
    {
        System.out.println("Connection was removed");  
        gamedata.removeplayer(conn.getId());
    }
}
