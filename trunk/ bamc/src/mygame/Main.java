package mygame;

import com.jme3.renderer.RenderManager;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.MouseInput;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {
    
    private BulletAppState bulletAppState;
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    //private RigidBodyControl landscape;
    //private RigidBodyControl floor_phy;
    private Node BlocksNode;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        setUpKeys();
        //setUpLight();
             
        /*  Loading the World:...
         *          
         */
        World world = new World(assetManager, bulletAppState);  
        world.LoadLevel("1337");                  
        
        viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
        flyCam.setMoveSpeed(100);
        
        /*  Create a Capsule, representing the Player
         *  (helps with the Physics-Stuff...)
         */        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, 250, 0));
        bulletAppState.getPhysicsSpace().add(player);
        
        BlocksNode = world.CreateNode();
        
        rootNode.attachChild(BlocksNode);
    }   

    private void setUpKeys() {
        inputManager.addMapping("Lefts",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups",    new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jumps",  new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Shoots",  new KeyTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Jumps");
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
        if (camDir.y>0.55)
        {
            camDir.y=0.50f;
            //cam.lookAtDirection(camDir, cam.getUp());
            //cam.lookAt(camDir, new Vector3f(0,-1,0));
        }
        if (camDir.y<-0.55)
        {
            camDir.y=-0.50f;
            //cam.lookAtDirection(camDir, cam.getUp());
            //cam.lookAt(camDir, new Vector3f(0,-1,0));
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
