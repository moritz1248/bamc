package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.renderer.RenderManager;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.network.Client;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {
    private bamc_client myserver = null;
    private DrawPlayers drawplayers = null;
    
    //private BulletAppState bulletAppState;
    //private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f hlcube1 = new Vector3f();
    private Vector3f hlcube2 = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    //private RigidBodyControl landscape;
    //private RigidBodyControl floor_phy;
    //private Node BlocksNode;
    private World world;
    private int add_x,add_y,add_z;
    private int del_x,del_y,del_z;
    //private Line line,line2;
    private HighlightBlock highlightblock1,highlightblock2;
    private Player player_;
    private static byte[] ip = new byte[4];
    
    private float tpf=0;

    public static void main(String[] args) {
        //System.err.close();
        if(args.length==4)
        {
            ip[0] = (byte)Integer.parseInt(args[0]);    
            ip[1] = (byte)Integer.parseInt(args[1]);   
            ip[2] = (byte)Integer.parseInt(args[2]);   
            ip[3] = (byte)Integer.parseInt(args[3]); 
            
            int ip1,ip2,ip3,ip4;
            
            if(ip[0]<0) 
                ip1=ip[0]+256; 
            else 
                ip1=ip[0];
            
            if(ip[1]<0) ip2=ip[1]+256; else ip2=ip[1];
            if(ip[2]<0) ip3=ip[2]+256; else ip3=ip[2];
            if(ip[3]<0) ip4=ip[3]+256; else ip4=ip[3];
            
            System.out.println("IP: "+ip1+"."+ip2+"."+ip3+"."+ip4);
            System.out.println("Viel Spass wuenscht euch: 47");
        }
        else
        {
            ip[0]=(byte)127;
            ip[1]=(byte)0;
            ip[2]=(byte)0;
            ip[3]=(byte)1;
        }
        
        Main app = new Main();
        app.setPauseOnLostFocus(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        add_x=add_y=add_z=del_x=del_y=del_z=-1;

        
        setUpKeys();

        world = new World(assetManager);  
        world.LoadLevel("1337");                  
        
        viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
        flyCam.setMoveSpeed(100);
        
        highlightblock1 = new HighlightBlock(assetManager);
        highlightblock2 = new HighlightBlock(assetManager);
        
        hlcube1.zero();
        hlcube2.zero();
        
        player_ = new Player(world);
        player_.SetStartPos(world.GetPlayerPos());
        player_.SetGravity(new Vector3f(0,1,0), 8f);
        
        rootNode.attachChild(world.CreateNode());  

        rootNode.attachChild(highlightblock1.GetGeometry());
        rootNode.attachChild(highlightblock2.GetGeometry());
        
        world.SetPlayerPos(player_.GetPos());
        /*
        Spatial gordon = assetManager.loadModel("Models/ogre/boxMesh.mesh.xml");
        
        gordon.setLocalTranslation(200, 200, 200);
        gordon.setLocalScale(0.05f);        
        gordon.rotate(-1.57f, 0, 0);
        */
        AmbientLight al = new AmbientLight();
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(dl);
        rootNode.addLight(al);
        
        //rootNode.attachChild(gordon);
        /*
        AnimControl control = gordon.getControl(AnimControl.class);

        AnimChannel channel = control.createChannel();
        channel.setAnim("walk");
        */
        initCrossHairs();
        
        drawplayers = new DrawPlayers(20, assetManager, player_);
        myserver = new bamc_client(ip[0], ip[1], ip[2], ip[3], world, drawplayers);
        
        
        rootNode.attachChild(drawplayers.GetNode());
    }   
   
    private void setUpKeys() {
        inputManager.addMapping("Lefts",   new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights",  new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups",     new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs",   new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jumps",   new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("RightM",  new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("LeftM",  new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Jumps");
        inputManager.addListener(this, "RightM");
        inputManager.addListener(this, "LeftM");
    }
    /** These are our custom actions triggered by key presses.
    * We do not walk yet, we just keep track of the direction the user pressed. */
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
          left = value;
        } else if (binding.equals("Rights")) {
          right = value;
        } else if (binding.equals("Ups")) {
          up = value;
        } else if (binding.equals("Downs")) {
          down = value;
        } else if (binding.equals("Jumps")) {
          if(!player_.isFalling())
            player_.SetJump(new Vector3f(0,1,0), 8f, 1.4f);
        } else if (binding.equals("RightM")) {
            if(value)
            {
                Vector3f pt = new Vector3f();
                pt.zero();

                pt = world.RayCastBlock(cam.getLocation(), cam.getDirection(), 10, false);

                if((pt.x!=-1) && (pt.y!=-1) && (pt.z!=-1))
                {
                    del_x=(int)pt.x;del_y=(int)pt.y;del_z=(int)pt.z;
                }    
            }
            
        } else if (binding.equals("LeftM")) {
            if(value)
            {
                Vector3f pt = new Vector3f();
                pt.zero();

                pt = world.RayCastBlock(cam.getLocation(), cam.getDirection(), 10, true);

                if((pt.x!=-1) && (pt.y!=-1) && (pt.z!=-1))
                {
                    add_x=(int)pt.x;add_y=(int)pt.y;add_z=(int)pt.z;
                    //add_y+=1;
                }
                
                if((add_x==(int)player_.GetPos().x) && (add_y==(int)player_.GetPos().y) && (add_z==(int)player_.GetPos().z))
                {
                    add_x=add_y=add_z=-1;
                }
                
            }            
        }
        
    }
    /**
    * This is the main event loop--walking happens here.
    * We check in which direction the player is walking by interpreting
    * the camera direction forward (camDir) and to the side (camLeft).
    * The setWalkDirection() command is what lets a physics-controlled player walk.
    * We also make sure here that the camera moves with player.
    */
    @Override
    public void simpleUpdate(float tpf) {
        this.tpf = tpf;
        
        Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left)  {walkDirection.addLocal(camLeft);
        }
        if (right) {walkDirection.addLocal(camLeft.negate());}
        if (up)    {walkDirection.addLocal(camDir);}
        if (down)  {walkDirection.addLocal(camDir.negate());}
        Vector3f walkdir = new Vector3f(0, 0, 0);
        walkdir.x=walkDirection.x;
        walkdir.y=0;
        walkdir.z=walkDirection.z;
        
        walkdir = walkdir.normalize();
        walkdir.x*=0.15f;
        walkdir.z*=0.15f;

        /*
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());         
        */
        
        //System.out.println(tpf);
        //System.out.println((1/tpf));
        player_.Update(tpf);
        player_.MoveTo(walkdir);
        
        //System.out.println(player_.GetPos());
        
        //player_.GetPos()
        
        cam.setLocation(new Vector3f(player_.GetPos().x,player_.GetPos().y+1.5f,player_.GetPos().z));          
        
        Vector3f pt = new Vector3f();
        pt.zero();

        pt = world.RayCastBlock(cam.getLocation(), cam.getDirection(), 10, false);
        
        if((pt.x!=-1) && (pt.y!=-1) && (pt.z!=-1))
        {
            if((pt.x!=hlcube1.x) || (pt.y!=hlcube1.y) || (pt.z!=hlcube1.z))
            {
                highlightblock1.Highlight((int)pt.x, (int)pt.y, (int)pt.z, 1.1f, ColorRGBA.Red);
                hlcube1.x=pt.x;hlcube1.y=pt.y;hlcube1.z=pt.z;
            }
            
            pt = world.RayCastBlock(cam.getLocation(), cam.getDirection(), 10, true);
            if((pt.x!=hlcube2.x) || (pt.y!=hlcube2.y) || (pt.z!=hlcube2.z))
            {
                highlightblock2.Highlight((int)pt.x, (int)pt.y, (int)pt.z, 1.1f, ColorRGBA.Yellow);
                hlcube2.x=pt.x;hlcube2.y=pt.y;hlcube2.z=pt.z;
            }
        }
        else
        {
            highlightblock1.Highlight(-5000, -5000, -5000, 0f, ColorRGBA.Red);
            highlightblock2.Highlight(-5000, -5000, -5000, 0f, ColorRGBA.Yellow);
            hlcube1 = hlcube1.zero();     
            hlcube2 = hlcube2.zero(); 
        }
        
        world.SetPlayerPos(new Vector3f(player_.GetPos().x,player_.GetPos().y+1.5f,player_.GetPos().z));
        
        //camDir.normalize();
        
        //Plane plane = new Plane(cam.getDirection(),1);
        
       
    }
    
    protected void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
        settings.getWidth()/2 - guiFont.getCharSet().getRenderedSize()/3*2,
        settings.getHeight()/2 + ch.getLineHeight()/2, 0);
        guiNode.attachChild(ch);
    }

    @Override
    public void simpleRender(RenderManager rm) {
           
        if((add_x!=-1) && (add_y!=-1) && (add_z!=-1))
        {
            Random rand = new Random();
            byte rbyte = (byte)rand.nextInt(5);
            //world.TryEnableBlock(add_x, add_y, add_z, rand.nextInt(4) + 0);
            world.SetBlockType(add_x, add_y, add_z, rbyte);
            myserver.SendBlockData(add_x, add_y, add_z, rbyte);
            add_x=add_y=add_z=-1;
        }
        
        if((del_x!=-1) && (del_y!=-1) && (del_z!=-1))
        {
            //world.TryDisableBlock(del_x, del_y, del_z);
            world.SetBlockType(del_x, del_y, del_z, (byte)-1);
            myserver.SendBlockData(del_x, del_y, del_z, (byte)-1);
            del_x=del_y=del_z=-1;
        }
                
        //cam.setFrustum(cam.getFrustumNear(), cam.getFrustumFar(), cam.getFrustumLeft(), cam.getFrustumRight(), cam.getFrustumTop(), cam.getFrustumBottom());
        //cam.setFrustum(0.7f, cam.getFrustumFar(), -0.6f, 0.6f, cam.getFrustumTop(), cam.getFrustumBottom());
        cam.setFrustumPerspective(65.0f,(float) 1280 / (float) 720, 0.1f, 1000);
        cam.onFrustumChange();
        
        cam.update();
        
        if(world.GetBlockGeom().UpdateNeeded())
            this.enqueue(world.GetBlockGeom());
        
        myserver.SendPlayerData(new Vector3f(player_.GetPos().x, player_.GetPos().y-0.1f, player_.GetPos().z ), cam.getDirection().clone());
       
        
        //drawplayers.reset();
        drawplayers.update(tpf);

            
    }
}
