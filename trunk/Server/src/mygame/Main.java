package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import com.jme3.network.base.DefaultServer;
import com.jme3.network.kernel.tcp.*;
import com.jme3.network.kernel.udp.*;
import com.jme3.network.message.StreamDataMessage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private SelectorKernel tcpkernel = null;
    private UdpKernel udpkernel = null;
    
    private DefaultServer server = null;
    
    private bamc_connectionlistener connectionlistener = null;
    private bamc_messageListener messageListener = null;
    
    private StreamDataMessage message = null;   
    
    private GameData gamedata = null;    
    
    private boolean startflag=false;

    public static void main(String[] args) {
        System.err.close();
        Main app = new Main();
        app.start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp() {
        try {
            
            gamedata = new GameData();          

            tcpkernel = new SelectorKernel(1337);
            udpkernel = new UdpKernel(1337);
            
            connectionlistener = new bamc_connectionlistener(gamedata);            
            
            server = new DefaultServer("BAMC", 1, tcpkernel, null);    
            
            messageListener = new bamc_messageListener(gamedata, server);
            
            server.addConnectionListener(connectionlistener);
            server.addMessageListener(messageListener);
            server.start();
            
            message = new StreamDataMessage();
            String sentstring = "Hello Client";
            message.setData(sentstring.getBytes());
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(server!=null)
            if(server.isRunning())
            {
                if(!startflag)
                {
                    startflag=true;
                    System.out.println("Server is running, start connecting!!");
                }
                
                Vector3f[] playerpos = gamedata.getplayerpos();
                Vector3f[] playerdir = gamedata.getplayerdir();
                
                if((playerpos.length>=1) && (playerdir.length==playerpos.length))
                {
                    //Sent out PlayerPositions:
                    
                    byte[] data = new byte[playerpos.length*24+2];

                    data[0] = 1;
                    
                    data[1] = (byte)playerpos.length;

                    byte[] tofloat = new byte[4];

                    for(int i=0;i<playerpos.length;i++)
                    {
                        if((playerpos[i].x!=0) && (playerpos[i].y!=0) && (playerpos[i].z!=0))
                        {  
                            tofloat = ByteFloatConvert.floatToByteArray(playerpos[i].x);

                            data[2+i*24] = tofloat[0];
                            data[3+i*24] = tofloat[1];
                            data[4+i*24] = tofloat[2];
                            data[5+i*24] = tofloat[3];

                            tofloat = ByteFloatConvert.floatToByteArray(playerpos[i].y);

                            data[6+i*24] = tofloat[0];
                            data[7+i*24] = tofloat[1];
                            data[8+i*24] = tofloat[2];
                            data[9+i*24] = tofloat[3];

                            tofloat = ByteFloatConvert.floatToByteArray(playerpos[i].z);

                            data[10+i*24] = tofloat[0];
                            data[11+i*24] = tofloat[1];
                            data[12+i*24] = tofloat[2];
                            data[13+i*24] = tofloat[3];
                            
                            tofloat = ByteFloatConvert.floatToByteArray(playerdir[i].x);

                            data[14+i*24] = tofloat[0];
                            data[15+i*24] = tofloat[1];
                            data[16+i*24] = tofloat[2];
                            data[17+i*24] = tofloat[3];

                            tofloat = ByteFloatConvert.floatToByteArray(playerdir[i].y);

                            data[18+i*24] = tofloat[0];
                            data[19+i*24] = tofloat[1];
                            data[20+i*24] = tofloat[2];
                            data[21+i*24] = tofloat[3];

                            tofloat = ByteFloatConvert.floatToByteArray(playerdir[i].z);

                            data[22+i*24] = tofloat[0];
                            data[23+i*24] = tofloat[1];
                            data[24+i*24] = tofloat[2];
                            data[25+i*24] = tofloat[3];
                            
                            //System.out.println("pos: "+playerpos[i]+" dir: "+playerdir[i]);
                        }
                    }

                    StreamDataMessage outmessage = new StreamDataMessage();                
                    outmessage.setData(data);
                    server.broadcast(outmessage);  
                }
            }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
