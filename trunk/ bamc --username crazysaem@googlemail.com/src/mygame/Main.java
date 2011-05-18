package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.renderer.RenderManager;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.input.controls.ActionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import jme3tools.optimize.GeometryBatchFactory;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {
    
    private BulletAppState bulletAppState;
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    private RigidBodyControl landscape;

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
             
        World world = new World(assetManager);  
        Node Boxes = new Node();
        world.LoadLevel("test");         
        Boxes = world.CreateLevelNode();   
        
        viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
        flyCam.setMoveSpeed(100);


        // We set up collision detection for the scene by creating a
        // compound collision shape and a static physics node with mass zero.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) Boxes);
        landscape = new RigidBodyControl(sceneShape, 0);
        // We set up collision detection for the player by creating
        // a capsule collision shape and a physics character node.
        // The physics character node offers extra settings for
        // size, stepheight, jumping, falling, and gravity.
        // We also put the player in its starting position.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, 500, 0));
        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
        
        //GeometryBatchFactory.optimize(rootNode);
        
        System.out.print(Boxes.getQuantity());
        
        rootNode.attachChild(Boxes);             
    }   

    private void setUpKeys() {
        inputManager.addMapping("Lefts",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups",    new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jumps",  new KeyTrigger(KeyInput.KEY_SPACE));
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
        if (left)  { walkDirection.addLocal(camLeft); }
        if (right) { walkDirection.addLocal(camLeft.negate()); }
        if (up)    { walkDirection.addLocal(camDir); }
        if (down)  { walkDirection.addLocal(camDir.negate()); }
        player.setWalkDirection(walkDirection);
        //cam.setLocation(player.getPhysicsLocation());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
