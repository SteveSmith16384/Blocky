package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.water.WaterFilter;

import mygame.blocks.BlockTerrainControl;
import mygame.blocks.ChunkControl;
import mygame.blocks.IBlockTerrainListener;
import mygame.blocktypes.DirtBlock;
import mygame.blocktypes.StoneBlock;
import mygame.util.Vector3Int;

/**
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

	private static final boolean RECORD_VID = false;

	public static void main(String[] args) {
		Main app = new Main();
		AppSettings settings = new AppSettings(true);
		app.setSettings(settings);
		settings.setSettingsDialogImage(null);
		app.start();
	}


	private Vector3f walkDirection = new Vector3f();
	private PhysicsCharacter player;
	private boolean left,right,up,down;


	@Override
	public void simpleInitApp() {
		//assetManager.registerLocator("assets/", FileLocator.class); // default

		final BulletAppState bas = new BulletAppState(PhysicsSpace.BroadphaseType.DBVT);
		bas.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
		this.stateManager.attach(bas);

		final BlockSettings blockSettings = new BlockSettings();
		blockSettings.setChunkSize(new Vector3Int(16, 16, 16));
		blockSettings.setBlockSize(1f);
		blockSettings.setMaterial(assetManager.loadMaterial("Materials/BlockyTexture.j3m"));
		blockSettings.setWorldSizeInChunks(new Vector3Int(50, 10, 50));

		BlockTerrainControl blocks = new BlockTerrainControl(blockSettings);

		this.cam.setFrustumFar(200f);
		//this.cam.setLocation(new Vector3f(100, 50, 100));
		//this.cam.lookAt(new Vector3f(50, 0, 50), Vector3f.UNIT_Y);

		player = new PhysicsCharacter(new CapsuleCollisionShape(1.5f, 4f), .2f);
		player.setJumpSpeed(20);
		player.setFallSpeed(30);
		player.setGravity(0);

		player.setPhysicsLocation(new Vector3f(50, 4, 50));
		bas.getPhysicsSpace().add(player);

		final Node terrain = new Node();
		terrain.addControl(blocks);
		bas.getPhysicsSpace().addAll(terrain);

		blocks.registerBlock(new StoneBlock());
		blocks.registerBlock(new DirtBlock());

		HeightMap heightmap = new ImageBasedHeightMap(assetManager.loadTexture("Textures/map1.png").getImage(), .2f);
		heightmap.load();
		blocks.loadFromHeightMap(new Vector3Int(0, 0, 0), heightmap, StoneBlock.class);		

		// Create a moon
		int diameter = 16;
		for (int z = 0; z < diameter; z++) {
			for (int y = 0; y < diameter; y++) {
				for (int x = 0; x < diameter; x++) {
					if (Math.sqrt((float) (x-diameter/2)*(x-diameter/2) + (y-diameter/2)*(y-diameter/2) + (z-diameter/2)*(z-diameter/2)) <= diameter/2) {
						blocks.setBlock(new Vector3Int(x+50, y+50, z+50), DirtBlock.class);
					}
				}
			}
		}

		blocks.addListener(new IBlockTerrainListener() {

			@Override
			public void onChunkUpdated(ChunkControl c) {
				Geometry geom = c.getGeometry();
				RigidBodyControl control = geom.getControl(RigidBodyControl.class);
				if(control == null){
					control = new RigidBodyControl(0);
					geom.addControl(control);
					bas.getPhysicsSpace().add(control);
				}
				control.setCollisionShape(new MeshCollisionShape(geom.getMesh()));

			}
		});

		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-5, -4, -4).normalizeLocal());

		rootNode.addLight(sun);

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(.5f));
		rootNode.addLight(al);

		//        sl.setSpotInnerAngle(50 * FastMath.DEG_TO_RAD);
		//        sl.setSpotOuterAngle(90 * FastMath.DEG_TO_RAD);
		//        sl.setSpotRange(blockSettings.getViewDistance() - 10);
		//        rootNode.addLight(sl);

		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		WaterFilter water = new WaterFilter(rootNode, sun.getDirection());
		water.setWaterHeight(5);
		fpp.addFilter(water);
		viewPort.addProcessor(fpp);

		BitmapText hudText = new BitmapText(guiFont, false);
		hudText.setSize(guiFont.getCharSet().getRenderedSize());
		hudText.setColor(ColorRGBA.White);
		hudText.setText("+");
		hudText.setLocalTranslation(this.settings.getWidth() / 2 - hudText.getLineWidth()/2, this.settings.getHeight() / 2 + hudText.getLineHeight(), 0); // position
		guiNode.attachChild(hudText);

		rootNode.attachChild(terrain);

		flyCam.setMoveSpeed(20);
		viewPort.setBackgroundColor(ColorRGBA.Black);

		inputManager.addListener(new ActionListener() {

			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed) {
					return;
				}
				CollisionResults results = new CollisionResults();

				Vector3f click3d = cam.getWorldCoordinates(new Vector2f(cam.getWidth() / 2, cam.getHeight() / 2), 0f).clone();
				Vector3f dir = cam.getWorldCoordinates(new Vector2f(cam.getWidth() / 2, cam.getHeight() / 2), 1f).subtractLocal(click3d).normalizeLocal();

				Ray ray = new Ray(click3d, dir);

				terrain.collideWith(ray, results);

				CollisionResult result = results.getClosestCollision();
				if (result != null) {
					Vector3f position = result.getContactPoint();
					Vector3Int blockPosition = blocks.getPointedBlockLocation(position);//, false);
					blocks.removeBlock(blockPosition);
				}


			}
		}, "toggle");
		inputManager.addMapping("toggle", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		setupKeys();

		if (RECORD_VID) {
			System.out.println("Recording video");
			VideoRecorderAppState video_recorder = new VideoRecorderAppState();
			stateManager.attach(video_recorder);
		}

	}

	private void setupKeys() {
		inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this,"Lefts");
		inputManager.addListener(this,"Rights");
		inputManager.addListener(this,"Ups");
		inputManager.addListener(this,"Downs");
		inputManager.addListener(this,"Space");
	}


	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);

		Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
		Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
		walkDirection.set(0,0,0);
		if(left)
			walkDirection.addLocal(camLeft);
		if(right)
			walkDirection.addLocal(camLeft.negate());
		if(up)
			walkDirection.addLocal(camDir);
		if(down)
			walkDirection.addLocal(camDir.negate());
		player.setWalkDirection(walkDirection);
		cam.setLocation(player.getPhysicsLocation());
	}



	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Lefts")) {
			left=isPressed;
		} else if (binding.equals("Rights")) {
			right=isPressed;
		} else if (binding.equals("Ups")) {
			up=isPressed;
		} else if (binding.equals("Downs")) {
			down=isPressed;
		} else if (binding.equals("Space")) {
			player.jump();
		}
	}

}
