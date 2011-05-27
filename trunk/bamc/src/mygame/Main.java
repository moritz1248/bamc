package mygame;

import com.jme3.renderer.RenderManager;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import java.util.Random;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {
    
    private BulletAppState bulletAppState;
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f hlcube = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    //private RigidBodyControl landscape;
    //private RigidBodyControl floor_phy;
    private Node BlocksNode;
    private World world;
    private int add_x,add_y,add_z;
    private int del_x,del_y,del_z;
    //private Line line,line2;
    private HighlightBlock highlightblock;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        add_x=add_y=add_z=del_x=del_y=del_z=-1;
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        setUpKeys();
        //setUpLight();
             
        /*  Loading the World:...
         *          
         */
        world = new World(assetManager, bulletAppState);  
        world.LoadLevel("1337");                  
        
        viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
        flyCam.setMoveSpeed(100);
        
        /*  Create a Capsule, representing the Player
         *  (helps with the Physics-Stuff...)
         */        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.8f, 2f, 1);
        //CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0f, 0f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(10);
        player.setFallSpeed(15);
        player.setGravity(15);
        //player.setPhysicsLocation(new Vector3f(0, 25, 0));
        player.setPhysicsLocation(world.GetPlayerPos());
        bulletAppState.getPhysicsSpace().add(player);
        
        //line = new Line(assetManager);
        //line2= new Line(assetManager);
        highlightblock = new HighlightBlock(assetManager);
        
        hlcube.zero();
        
        rootNode.attachChild(world.CreateNode());  
        
        //line.SetPoints(new Vector3f(0,0,0), new Vector3f(100,100,100));
        //line2.SetPoints(new Vector3f(0,0,0), new Vector3f(100,100,0));
        
        //rootNode.attachChild(line.GetGeom());
        //rootNode.attachChild(line2.GetGeom());
        rootNode.attachChild(highlightblock.GetGeometry());
        
        initCrossHairs();
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
          player.jump();
        } else if (binding.equals("RightM")) {
            if(value)
            {
                Vector3f pt = new Vector3f();
                pt.zero();

                pt = world.RayCastBlock(cam.getLocation(), cam.getDirection(), 10);

                if((pt.x!=-1) && (pt.y!=-1) && (pt.z!=-1))
                {
                    del_x=(int)pt.x;del_y=(int)pt.y;del_z=(int)pt.z;
                    //del_y=del_y-1;
                }    
                /*
                Vector3f pt1 = new Vector3f();
                pt1.zero();

                Vector3f pt2 = new Vector3f();
                pt2.zero();

                pt1.x=cam.getLocation().x;
                pt1.y=cam.getLocation().y;
                pt1.z=cam.getLocation().z;


                pt2.x=cam.getLocation().x + (cam.getDirection().x*50);
                pt2.y=cam.getLocation().y + (cam.getDirection().y*50);
                pt2.z=cam.getLocation().z + (cam.getDirection().z*50);
                 * 
                line.SetPoints(pt1, pt2);
                */
            }
            
        } else if (binding.equals("LeftM")) {
            if(value)
            {
                Vector3f pt = new Vector3f();
                pt.zero();

                pt = world.RayCastBlock(cam.getLocation(), cam.getDirection(), 10);

                if((pt.x!=-1) && (pt.y!=-1) && (pt.z!=-1))
                {
                    add_x=(int)pt.x;add_y=(int)pt.y;add_z=(int)pt.z;
                    add_y+=1;
                }
                /*
                Vector3f pt1 = new Vector3f();
                pt1.zero();

                Vector3f pt2 = new Vector3f();
                pt2.zero();

                pt1.x=cam.getLocation().x;
                pt1.y=cam.getLocation().y;
                pt1.z=cam.getLocation().z;


                pt2.x=cam.getLocation().x + (cam.getDirection().x*50);
                pt2.y=cam.getLocation().y + (cam.getDirection().y*50);
                pt2.z=cam.getLocation().z + (cam.getDirection().z*50);

                line.SetPoints(pt1, pt2);
                */
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
        Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left)  {walkDirection.addLocal(camLeft);
        //System.out.println("x: "+camDir.x+" y: "+camDir.y+" z: "+camDir.z);
        }
        if (right) {walkDirection.addLocal(camLeft.negate());}
        if (up)    {walkDirection.addLocal(camDir);}
        if (down)  {walkDirection.addLocal(camDir.negate());}
        Vector3f walkdir = walkDirection;
        walkdir.y=0; 
        walkdir.normalize();
        walkdir.x*=0.3f;
        walkdir.z*=0.3f;

        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
        
        //System.out.println(cam.getLocation());   
        
        Vector3f pt = new Vector3f();
        pt.zero();

        pt = world.RayCastBlock(cam.getLocation(), cam.getDirection(), 10);
        
        if((pt.x!=-1) && (pt.y!=-1) && (pt.z!=-1))
        {
            if((pt.x!=hlcube.x) || (pt.y!=hlcube.y) || (pt.z!=hlcube.z))
            {
                highlightblock.Highlight((int)pt.x, (int)pt.y, (int)pt.z, 1.1f, ColorRGBA.Red);
                hlcube.x=pt.x;hlcube.y=pt.y;hlcube.z=pt.z;
            }
        }
        else
        {
            highlightblock.Highlight(0, 0, 0, 0f, ColorRGBA.Red);
            hlcube.zero();            
        }
       
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
        //world.TryDisableBlock(0, 0, 0);
            
        if((add_x!=-1) && (add_y!=-1) && (add_z!=-1))
        {
            Random rand = new Random();
            world.TryEnableBlock(add_x, add_y, add_z, rand.nextInt(4) + 0);
            add_x=add_y=add_z=-1;
        }
        
        if((del_x!=-1) && (del_y!=-1) && (del_z!=-1))
        {
            world.TryDisableBlock(del_x, del_y, del_z);
            del_x=del_y=del_z=-1;
        }
    }
}
